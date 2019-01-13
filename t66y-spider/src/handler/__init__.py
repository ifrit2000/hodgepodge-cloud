from bs4 import BeautifulSoup

from log import LoggerObject


class Handler(LoggerObject):

    def __init__(self, downloader):
        super().__init__("handler")
        self.__downloader = downloader

    def handle(self, url):
        html = self.__downloader.get(url)
        self.logger.debug("handle html of %s" % url)
        return self._handle_html(html, url)

    def _handle_html(self, html, url):
        pass


class PageHandler(Handler):

    def _handle_html(self, html, url):
        topic_list = list()
        soup = BeautifulSoup(html, "html.parser")
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

    def _handle_html(self, html, url):
        topic = dict()
        topic["status"] = "1"
        soup = BeautifulSoup(html, "html.parser")
        if soup.select(".tpc_content img") is None:
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
