import json
import os
from concurrent.futures import ThreadPoolExecutor

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
            self.__page_list = list(range(1, 101))

    def run(self):
        self.__process()

    def get_page_range(self):
        first_page_num = self.__page_list[0]
        last_page_num = self.__page_list[len(self.__page_list) - 1]
        step = ((last_page_num - first_page_num) + 1) / 4

        pass

    def __process_page(self):
        self.logger.info("process page:")
        with ThreadPoolExecutor(self.__thread_num) as executor:
            for fid in self.__fid_list:

                for pageNum in self.__page_list:
                    url = os.path.join(self.__base_url, "thread0806.php?fid=%s&page=%s" % (fid, pageNum))
                    executor.submit(self.__handler.handle, url)

    def __process_topic(self):
        pass
