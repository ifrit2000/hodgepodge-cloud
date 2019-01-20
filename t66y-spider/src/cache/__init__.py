import pymysql
import redis

from datasource import MySql
from log import LoggerObject


class Cache(LoggerObject):

    def __init__(self):
        super().__init__("Cache")

    def is_contain_url(self, url):
        pass

    def add_urls(self, urls):
        pass


class MemCache(Cache):
    __urls = None

    def __init__(self, datasource):
        super().__init__()
        self.__init(datasource)

    def __init(self, datasource):
        if self.__urls is None:
            self.__urls = set(datasource.find_all_url())

    def is_contain_url(self, url):
        return url in self.__urls

    def add_urls(self, urls):
        self.__urls = self.__urls | urls


class RedisCache(Cache):
    def __init__(self, datasource, cache_type="url", **kw):
        super().__init__()
        self.__cache_type = cache_type
        self.__cache_key = "T66Y.URLS"
        self.__cache_key_image = "T66Y.IMAGE_FLAG"
        self.__cache_key_torrent = "T66Y.TORRENT_FLAG"
        self.__init(datasource, **kw)

    def __init(self, datasource, **kw):
        self.__redis = redis.Redis(host=kw.get("host"), port=kw.get("port"), db=kw.get("db"))
        self.clear_cache()
        self.logger.info("cache %s" % self.__cache_type)
        if self.__cache_type == 'url':
            self.add_urls(datasource.find_all_url())
        elif self.__cache_type == 'image':
            self.add_file_flags(datasource.find_all_downloaded_file(self.__cache_type))
        elif self.__cache_type == 'torrent':
            self.add_file_flags(datasource.find_all_downloaded_file(self.__cache_type))

    def is_contain_url(self, url):
        return self.__redis.sismember(self.__cache_key, url)

    def add_urls(self, urls):
        if urls is not None and len(urls) > 0:
            self.__redis.sadd(self.__cache_key, *urls)

    def add_file_flags(self, file_flags):
        if file_flags is not None and len(file_flags) > 0:
            if self.__cache_type == 'image':
                self.__redis.sadd(self.__cache_key_image, *file_flags)
            if self.__cache_type == 'torrent':
                self.__redis.sadd(self.__cache_key_torrent, *file_flags)

    def is_contain_file_flag(self, file_flag):
        if self.__cache_type == 'image':
            return self.__redis.sismember(self.__cache_key_image, file_flag)
        if self.__cache_type == 'torrent':
            return self.__redis.sismember(self.__cache_key_torrent, file_flag)

    def clear_cache(self):
        self.__redis.delete(self.__cache_key)
        self.__redis.delete(self.__cache_key_torrent)
        self.__redis.delete(self.__cache_key_image)
        self.logger.info("clear cache")


if __name__ == '__main__':
    mysqlConfig = {
        "host": "172.28.0.4",
        "port": 3306,
        "user": "appdata",
        "password": "123456",
        "db": "t66y",
        "charset": "utf8",
        "cursorclass": pymysql.cursors.DictCursor,
        "use_unicode": True
    }
    mysql = MySql(**mysqlConfig)
    redis = RedisCache(mysql, host="172.28.0.2", port=6379, db=10)

    # print(redis.is_contain_url("htm_data/4/1812/3373960.html"))
    # redis.add_urls([])
