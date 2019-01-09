import json

from handler import HtmlResponseHandler, TopicHandler, PageHandler
from http_request import Downloader
from log import LoggerObject


class Spider(LoggerObject):
    def __init__(self, config=None):
        super().__init__(name="spider")
        self.__downloader = Downloader(HtmlResponseHandler)
        if config is not None:
            self.__config = json.loads(config)
        self.__target = config.get("target")
        downloader = Downloader(HtmlResponseHandler())
        if self.__target == "topic":
            self.__thread_num = config.get("threadNum", 5)
            self.__handler = TopicHandler(downloader)
            self.__process = self.__process_topic
        elif self.__target == "page":
            self.__thread_num = config.get("threadNum", 1)
            self.__fid_list = config.get("fidList", ["2", "4", "5", "15", "25", "26", "27"])
            self.__handler = PageHandler(downloader)
            self.__process = self.__process_page

    def run(self):
        self.__process()

    def __process_page(self):
        pass

    def __process_topic(self):
        pass
