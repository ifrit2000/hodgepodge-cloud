import pymysql
import redis

from log import LoggerObject


class MySql(LoggerObject):
    def __init__(self, **kw):
        super().__init__("mysql")
        self.__connection = pymysql.connect(**kw)

    @property
    def connection(self):
        return self.__connection

    def find_all_url(self):
        urls = list()
        try:
            with self.connection.cursor() as cursor:
                cursor.execute("select topic_url from TOPIC_INFO")
                urls = set([row["topic_url"] for row in cursor.fetchall()])
        except RuntimeError as re:
            self.logger.error("find all url failed")
        return urls


class Redis(LoggerObject):
    def __init__(self):
        super().__init__("mysql")
        redis.Redis(host='192.168.0.110', port=6379, db=0)
