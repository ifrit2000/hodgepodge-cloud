from log import LoggerObject


class Cache(LoggerObject):
    __urls = None

    def __init__(self, datasource):
        super().__init__("Cache")
        self.__init(datasource)

    @classmethod
    def __init(cls, datasource):
        if cls.__urls is None:
            cls.__urls = datasource.find_all_url()

    @classmethod
    def is_contain_url(cls, url):
        return url in cls.__urls

    @classmethod
    def add_url(cls, url):
        cls.__urls.add(url)
