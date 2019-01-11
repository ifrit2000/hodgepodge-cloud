from log import LoggerObject


class Cache(LoggerObject):
    def __init__(self):
        super().__init__("Cache")

    __urls = set()

    @classmethod
    def is_url_exist(cls, url):
        return url in cls.__urls

    @classmethod
    def add_url(cls, url):
        cls.__urls.add(url)
