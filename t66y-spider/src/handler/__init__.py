import hashlib
import os
from urllib.parse import urlsplit

from bs4 import BeautifulSoup

from http_request import FileResponseProcessor
from log import LoggerObject


class Handler(LoggerObject):

    def __init__(self, downloader):
        super().__init__("handler")
        self._downloader = downloader

    def handle(self, url):
        html = self._downloader.get(url)
        self.logger.debug("handle html of %s" % url)
        return self._handle(html, url)

    def _handle(self, html, url):
        pass


class PageHandler(Handler):

    def _handle(self, html, url):
        topic_list = list()
        soup = BeautifulSoup(html, "html5lib")
        table = soup.find(name="table", id="ajaxtable")
        if table is None:
            self.logger.error("handle page error: cannot find table\n%s" % url)
            return topic_list
        # 判断是否首页
        try:
            topic_start_flag = table.find_all(name="tr", attrs={"class": "tr2"})
            topic_start_flag = topic_start_flag[len(topic_start_flag) - 1]
            for tr in topic_start_flag.find_next_siblings(name="tr", attrs={"class": "tr3 t_one tac"}):
                a = tr.find(name="h3").a
                title = a.text.replace("'", "\\\'").replace('"', '\\\"')
                topic_list.append({"url": a['href'], "title": title})
        except Exception as e:
            self.logger.error("handle page error:\n%s" % url)
        return topic_list


class TopicHandler(Handler):

    def _handle(self, html, url):
        topic = dict()
        topic["url"] = url
        topic["status"] = "1"
        soup = BeautifulSoup(html, "html5lib")
        if len(soup.select(".t")) == 0:
            print(soup.select(".tpc_content do_not_catch"))
        if len(soup.select(".tpc_content")) == 0:
            topic["status"] = "4"
            self.logger.error("handle topic error:\n%s" % url)
            return topic
        try:
            get_image = lambda tag: list(
                map(lambda image: image[tag].replace(".th", ""),
                    filter(lambda image: image.get(tag) is not None and (
                            image[tag].endswith(".jpg") or image[tag].endswith(".JPG") or image[tag].endswith(
                        ".jpeg") or image[tag].endswith(".png")),
                           soup.select(".tpc_content img"))))
            topic["images"] = list(set(get_image("data-src")) | set(get_image("src")))
            topic["torrent_links"] = list(
                map(lambda a: a.text, filter(lambda a: "hash=" in a.text, soup.select(".tpc_content a"))))
            if topic["images"] is None or len(topic["images"]) == 0:
                topic["status"] = "2"  # image error
            if topic["torrent_links"] is None or len(topic["torrent_links"]) == 0:
                topic["status"] = "3"  # torrent_error
        except Exception as e:
            self.logger.error("handle topic error:\n%s" % url)
            topic["status"] = "4"
        return topic


class FileHandler(Handler):
    def __init__(self, downloader, filepath):
        super().__init__(downloader)
        self.__filepath = filepath

    def _handle_file(self, data, url):
        try:
            sha512 = hashlib.sha512()
            sha512.update(data)
            file_id = sha512.hexdigest()
            with open(os.path.join(self.__filepath, file_id), 'wb') as file:
                file.write(data)
            return True, self.__filepath, file_id
        except Exception as e:
            self.logger.error("get file failed:%s", url)
            return False, None, None


class ImageHandler(FileHandler):

    def _handle(self, data, url):
        return self._handle_file(data, url)


class TorrentHandler(FileHandler):
    def __init__(self, downloader, filepath):
        super().__init__(downloader, filepath)
        self.__response_processor = FileResponseProcessor()

    def _handle(self, data, url):
        # self.__downloader.get(url)
        file_data = None
        if "rmdown" in url:
            file_data = self.__handle_rmdown(data, url)
        if "xhh8" in url:
            file_data = self.__handle_xhh8(data, url)
        if file_data is None:
            return False
        return self._handle_file(file_data, url)

    def __handle_rmdown(self, html, url):
        self.logger.debug("handle rmdown")
        soup = BeautifulSoup(html, "html5lib")
        param = dict()
        for item in map(lambda input_field: {input_field["name"]: input_field["value"]}, soup.form.find_all("input")):
            param.update(item)
        return self._downloader.get(urlsplit(url).netloc + "/" + soup.form.get("action"), fields=param,
                                    response_processor=self.__response_processor)

    def __handle_xhh8(self, html, url):
        self.logger.debug("handle xhh8")
        soup = BeautifulSoup(html, "html5lib")
        param = dict()
        for item in map(lambda input_field: {input_field["name"]: input_field["value"]}, soup.form.find_all("input")):
            param.update(item)
        return self._downloader.post(url, fields=param, response_processor=self.__response_processor)
