import os
from urllib.parse import urlparse, parse_qs

from spider.http_connection import HttpConnection
from spider.parser import Parser


class Spider(object):

    def __init__(self):
        self.__connection = HttpConnection()

    def test(self, url):
        self.run(url)

    def run(self, page_url):
        topic_list = self.__topic_list(page_url)
        # get forum area
        area = parse_qs(urlparse(page_url).query)['fid']
        for topic in topic_list:
            topic.area = area
            self.__handle_topic(topic)
        return topic_list

    def __topic_list(self, url):
        page = self.__connection.get(url)
        return Parser.topic_from_page(page)

    def __handle_topic(self, topic):
        print(os.getpid(), "handle %s" % topic.title)
        topic_html = self.__connection.get("http://www.t66y.com/" + topic.url)
        Parser.element_from_topic(topic, topic_html)
        return topic
