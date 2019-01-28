import json
import math
import os
import random
import signal
import sys
import time
from concurrent.futures import ThreadPoolExecutor, as_completed

from cache import RedisCache
from datasource import MySql
from handler import TopicHandler, PageHandler, ImageHandler, TorrentHandler
from http_request import HtmlResponseProcessor, FileResponseProcessor, Downloader
from log import LoggerObject


class Spider(LoggerObject):

    def __init__(self, config=None, config_file=None):
        super().__init__(name="spider")
        self.__flag = True
        self.__config = config
        if config_file is not None:
            file_config = json.loads(config_file)
            file_config.update(self.__config)
            self.__config = file_config
        self.__mysql = MySql(**(config.get("mysqlConfig")))
        self.__target = config.get("target")
        self.__thread_num = int(config.get("threadNum", 1))
        self.__fid_list = config.get("fidList", ["2", "4", "5", "15", "25", "26", "27"])
        downloader = Downloader(headers=config.get("headers"), proxy_url=config.get("proxy_url"))
        if self.__target == "topic" or self.__target == "page":
            self.__base_url = config.get("baseUrl", "www.t66y.com")
            downloader.response_processor = HtmlResponseProcessor()
            if self.__target == "topic":
                self.__handler = TopicHandler(downloader)
                self.__process = self.__process_topic
                self.__batch_count = config.get("batchCount", 15)
            elif self.__target == "page":
                self.__handler = PageHandler(downloader)
                self.__process = self.__process_page
                self.__cache = RedisCache(self.__mysql, **config["redisConfig"])
                self.__handled_page = dict()
        elif self.__target == "image" or self.__target == "torrent":
            self.__base_file_path = config.get("filePath", "/tmp")
            self.__process = self.__process_file
            self.__batch_count = config.get("batchCount", 15)
            self.__cache = RedisCache(self.__mysql, self.__target, **config["redisConfig"])
            if self.__target == "image":
                downloader.response_processor = FileResponseProcessor()
                self.__handler = ImageHandler(downloader)
            if self.__target == "torrent":
                downloader.response_processor = HtmlResponseProcessor(charset="utf-8")
                self.__handler = TorrentHandler(downloader)

    def __interrupt(self, signum, frame):
        self.__flag = False
        self.logger.info("用户中断，等待剩余线程完成任务")

    def run(self):
        signal.signal(signal.SIGINT, self.__interrupt)
        self.logger.info("start")
        self.__process()
        self.logger.info("finished")

    def __get_page_range(self, first_page_num, last_page_num, fid):
        if first_page_num == last_page_num:
            return first_page_num
        middle_page_num = first_page_num + math.ceil(((last_page_num - first_page_num) + 1.0) / 5.0) - 1
        if self.__is_handle(middle_page_num, fid):
            return self.__get_page_range(first_page_num, middle_page_num, fid)
        else:
            return self.__get_page_range(middle_page_num + 1, last_page_num, fid)

    def __is_handle(self, page_num, fid):
        time.sleep(random.randint(15, 25) / 10.0)
        url = os.path.join(self.__base_url, "thread0806.php?fid=%s&page=%s" % (fid, page_num))
        topic_list = self.__handler.handle(url)
        handle_flag = True
        for topic in topic_list:
            if not self.__cache.is_contain_url(topic["url"]):
                handle_flag = False
        self.__handled_page[page_num] = topic_list
        return handle_flag

    def __process_page(self):
        self.logger.info("process page:")
        with ThreadPoolExecutor(self.__thread_num) as executor:
            for fid in self.__fid_list:
                self.logger.info("get last page not processed")
                last_page = self.__get_page_range(1, 100, fid)
                self.logger.info("last page: %s", str(last_page))
                self.logger.info("handled page: %s", list(self.__handled_page))
                tasks = list()
                result = list()
                for pageNum in range(1, last_page + 1):
                    if pageNum in self.__handled_page.keys():
                        result.append(self.__handled_page.get(pageNum))
                    else:
                        url = os.path.join(self.__base_url, "thread0806.php?fid=%s&page=%s" % (fid, pageNum))
                        time.sleep(random.randint(18, 25) / 10.0)
                        tasks.append(executor.submit(self.__handler.handle, url))
                for future in as_completed(tasks):
                    task_result = future.result()
                    result.append(task_result)
                # filter all handled topic
                all_topics = list()
                url_list = set()
                for topic_list in result:
                    for topic in topic_list:
                        if not self.__cache.is_contain_url(topic.get("url")):
                            topic['fid'] = fid
                            all_topics.append(topic)
                            url_list.add(topic.get("url"))
                self.logger.info("fid %s get %s topics", fid, len(all_topics))
                self.logger.info("write to db")
                self.__mysql.insert_topic_list(all_topics)
                self.__cache.add_urls(url_list)
                if not self.__flag:
                    break
                # with shelve.open(fid) as db:
                #     db[fid] = all_topics

    def __process_topic(self):
        while True and self.__flag:
            not_handle_topic_list = self.__mysql.get_topic_by_status('0', self.__batch_count, self.__fid_list)
            if len(not_handle_topic_list) == 0:
                break
            topic_list = list()
            all_task = list()
            topic_dict = dict()
            for t in not_handle_topic_list:
                topic_dict[t.get("url")] = t
            self.logger.info("handle %s topics", str(len(not_handle_topic_list)))
            with ThreadPoolExecutor(self.__thread_num) as executor:
                for topic in not_handle_topic_list:
                    time.sleep(random.randint(18, 25) / 10.0)
                    task = executor.submit(self.__handler.handle, os.path.join(self.__base_url, topic.get("url")))
                    all_task.append(task)
                for future in as_completed(all_task):
                    result = future.result()
                    url = result.get("url").replace(self.__base_url + "/", "")
                    topic = topic_dict[url]
                    topic.update(result)
                    topic["url"] = url
                    topic_list.append(topic)
            self.__mysql.update_topic_list(topic_list)

    def __process_file(self):
        count = 0
        while True and self.__flag:
            file_urls = self.__mysql.get_file_url(num=self.__batch_count, target=self.__target.upper(),
                                                  fid_list=self.__fid_list)
            if len(file_urls) == 0:
                break
            with ThreadPoolExecutor(self.__thread_num) as executor:
                task_list = list()
                count = 0
                for file_url in file_urls:
                    time.sleep(random.randint(3, 8) / 10.0)
                    task = executor.submit(self.__start_process_file, file_url.get("topicUrl"), file_url.get("fileUrl"),
                                           file_url.get("fileFlag"))
                    task_list.append(task)
                for future in as_completed(task_list):
                    pass
            count = count + 1
            self.logger.info("Finish %d round" % count)

    def __start_process_file(self, topic_url, file_url, file_flag):
        if self.__cache.is_contain_file_flag(file_flag):
            # exist file
            self.logger.debug("exist %s", file_url)
            _id, path = self.__mysql.get_file_id_and_path_by_flag(file_flag, self.__target)
            self.__mysql.write_back_file_info(target=self.__target.upper(), topic_url=topic_url,
                                              file_url=file_url, file_path=path, file_id=_id,
                                              file_status="1")
            return
        file_id, data = self.__handler.handle(file_url)
        dir_name = os.path.split(os.path.split(topic_url)[0])[1]
        file_path = os.path.join(dir_name, self.__target)
        if file_id is not None and data is not None:
            try:
                full_file_path = os.path.join(self.__base_file_path, file_path)
                if not os.path.exists(full_file_path):
                    os.makedirs(full_file_path)
                with open(os.path.join(full_file_path, file_id), 'wb') as file:
                    file.write(data)
                self.__mysql.write_back_file_info(target=self.__target.upper(), topic_url=topic_url,
                                                  file_url=file_url, file_path=file_path, file_id=file_id,
                                                  file_status="1")
                self.__cache.add_file_flags((file_flag,))
            except PermissionError as pe:
                self.logger.error("permission err %s" % self.__base_file_path)
                sys.exit(-1)
            except Exception as e:
                self.logger.error("error download file: %s", file_url)
                self.__mysql.write_back_file_info(self.__target.upper(), topic_url, file_url, "-", "-", "2")
        else:
            self.__mysql.write_back_file_info(self.__target.upper(), topic_url, file_url, "-", "-", "2")
        return
