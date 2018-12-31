from bs4 import BeautifulSoup


class Parser(object):

    def __init__(self):
        pass

    @staticmethod
    def topic_from_page(page_html):
        topic_list = []
        soup = BeautifulSoup(page_html, "html.parser")

        table = soup.find(name="table", id="ajaxtable")
        # 判断是否首页
        topic_start_flag = table.find_all(name="tr", attrs={"class": "tr2"})
        topic_start_flag = topic_start_flag[len(topic_start_flag) - 1]
        for tr in topic_start_flag.find_next_siblings(name="tr", attrs={"class": "tr3 t_one tac"}):
            a = tr.find(name="h3").a
            topic_list.append(Topic(url=a['href'], title=a.text))
        return topic_list

    @staticmethod
    def element_from_topic(topic, topic_html):
        soup = BeautifulSoup(topic_html, "html.parser")
        images = list(
            map(lambda image: image['data-src'].replace(".th", ""),
                filter(lambda image: image['data-src'].endswith(".jpg"), soup.select(".tpc_content img"))))
        topic.images = images
        torrent_links = list(map(lambda a: a.text, filter(lambda a: "hash=" in a.text, soup.select(".tpc_content a"))))
        topic.torrent_links = torrent_links
        return topic


class Topic(object):

    def __init__(self, url=None, title=None, images=None, torrent_links=None):
        self.__url = url
        self.__title = title
        self.__images = images
        self.__torrent_links = torrent_links

    @property
    def url(self):
        return self.__url

    @url.setter
    def url(self, url):
        self.__url = url

    @property
    def title(self):
        return self.__title

    @title.setter
    def title(self, title):
        self.__title = title

    @property
    def images(self):
        return self.__images

    @images.setter
    def images(self, images):
        self.__images = images

    @property
    def torrent_links(self):
        return self.__torrent_links

    @torrent_links.setter
    def torrent_links(self, torrent_links):
        self.__torrent_links = torrent_links
