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
        urls = None
        try:
            with self.connection.cursor() as cursor:
                cursor.execute("select TOPIC_URL from TOPIC_INFO")
                urls = [row["TOPIC_URL"] for row in cursor.fetchall()]
        except RuntimeError as re:
            self.logger.error("find all url failed")
        return urls

    def insert_topic_list(self, topic_list):
        if topic_list is None or len(topic_list) == 0:
            return
        sql_template = """insert into t66y.TOPIC_INFO(TOPIC_URL, TOPIC_FID, TOPIC_TITLE) 
        VALUES %s"""
        try:
            with self.connection.cursor() as cursor:
                topic_values = ""
                for topic in topic_list:
                    topic_values = topic_values + "('%s','%s','%s')," % (
                        topic.get("url"), topic.get("area"), topic.get("title"))

                count = cursor.execute((sql_template % topic_values).rstrip(","))
                self.connection.commit()
                self.logger.info("insert %s topics"% count)
        except Exception as e:
            self.logger.error("insert error")
            self.connection.rollback()

    def update_topic_list(self, topic_list):
        pass


class Redis(LoggerObject):
    def __init__(self):
        super().__init__("mysql")
        redis.Redis(host='192.168.0.110', port=6379, db=0)
