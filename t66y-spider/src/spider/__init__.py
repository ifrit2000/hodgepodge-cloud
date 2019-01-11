import json
import math
import os
import random
import shelve
import time
from concurrent.futures import ThreadPoolExecutor, as_completed

from cache import Cache
from datasource import MySql
from handler import TopicHandler, PageHandler
from http_request import Downloader, HtmlResponseProcessor
from log import LoggerObject


class Spider(LoggerObject):
    def __init__(self, config=None, config_file=None):
        super().__init__(name="spider")
        self.__config = config
        if config_file is not None:
            file_config = json.loads(config_file)
            file_config.update(self.__config)
            self.__config = file_config
        self.__mysql = MySql(**(config.get("mysqlConfig")))

        self.__target = config.get("target")
        self.__base_url = config.get("baseUrl", "www.t66y.com")
        self.__thread_num = int(config.get("threadNum", 1))
        downloader = Downloader(HtmlResponseProcessor(), headers=config.get("headers"))
        if self.__target == "topic":
            self.__handler = TopicHandler(downloader)
            self.__process = self.__process_topic
        elif self.__target == "page":
            self.__fid_list = config.get("fidList", ["2", "4", "5", "15", "25", "26", "27"])
            self.__handler = PageHandler(downloader)
            self.__process = self.__process_page
            self.__cache = Cache(self.__mysql)
            self.__handled_page = dict()

    def run(self):
        self.__process()

    def __get_page_range(self, first_page_num, last_page_num, fid):
        if first_page_num == last_page_num:
            return first_page_num
        middle_page_num = first_page_num + math.ceil(((last_page_num - first_page_num) + 1.0) / 5.0) - 1
        if self.__is_handle(middle_page_num, fid):
            return self.__get_page_range(first_page_num, middle_page_num, fid)
        else:
            return self.__get_page_range(middle_page_num + 1, last_page_num, fid)

    # 判断是否被处理
    def __is_handle(self, page_num, fid):
        url = os.path.join(self.__base_url, "thread0806.php?fid=%s&page=%s" % (fid, page_num))
        topic_list = self.__handler.handle(url)
        handle_flag = True
        for topic in topic_list:
            if not self.__cache.is_contain_url(topic["url"]):
                handle_flag = False
        if not handle_flag:
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
                        tasks.append(executor.submit(self.__handler.handle, url))
                        time.sleep(random.randint(15, 25) / 10.0)
                for future in as_completed(tasks):
                    task_result = future.result()
                    result.append(task_result)
                # filter all handled topic
                all_topics = list()
                for topic_list in result:
                    for topic in topic_list:
                        if not self.__cache.is_contain_url(topic.get("url")):
                            all_topics.append(topic)
                self.logger.info("fid %s get %s topics", fid, len(all_topics))
                with shelve.open(fid) as db:
                    db[fid] = all_topics

    def __process_topic(self):
        pass
