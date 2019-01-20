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
                        topic.get("url"), topic.get("fid"), topic.get("title"))

                count = cursor.execute((sql_template % topic_values).rstrip(","))
                self.connection.commit()
                self.logger.info("insert %s topics" % count)
        except Exception as e:
            self.logger.error("insert error")
            self.connection.rollback()

    def update_topic_list(self, topic_list):
        images_sql_template = """insert into t66y.IMAGE_INFO(TOPIC_URL, IMAGE_URL) 
        VALUE %s"""
        torrent_sql_template = """insert into t66y.TORRENT_INFO(TOPIC_URL, TORRENT_URL, TORRENT_HASH) 
        VALUE %s"""
        sql = ""
        try:
            with self.__connection.cursor() as cursor:
                for topic in topic_list:
                    cursor.execute("update TOPIC_INFO set TOPIC_STATUS='%s' where TOPIC_URL='%s'"
                                   % (topic.get("status"), topic.get("url")))
                    image_value = ""
                    for image in topic.get("images", list()):
                        image_value = image_value + "('%s','%s')," % (topic.get("url"), image)
                    if image_value != "":
                        sql = (images_sql_template % image_value).rstrip(",")
                        cursor.execute(sql)

                    torrent_value = ""
                    for torrent_link in topic.get("torrent_links", list()):
                        torrent_hash = torrent_link[torrent_link.find("hash=") + len("hash="):]
                        torrent_value = torrent_value + "('%s','%s','%s')," % (
                            topic.get("url"), torrent_link, torrent_hash)
                    if torrent_value != "":
                        sql = (torrent_sql_template % torrent_value).rstrip(",")
                        cursor.execute(sql)
                self.connection.commit()
        except Exception as e:
            self.logger.error("update topic failed, sql: %s" % sql)
            self.connection.rollback()
        self.logger.info("update %s topics success" % str(len(topic_list)))

    @staticmethod
    def __build_fid_segment(fid_list):
        fid_segment = ''
        if fid_list is not None:
            if isinstance(fid_list, list) or isinstance(fid_list, tuple):
                fid_segment = "TOPIC_FID in ('%s') and" % "','".join(fid_list)
            if isinstance(fid_list, str):
                fid_segment = "TOPIC_FID = '%s' and" % fid_list
        return fid_segment

    def get_topic_by_status(self, status, num, fid_list=None):
        if isinstance(num, int):
            num = str(num)
        fid_segment = self.__build_fid_segment(fid_list)
        with self.connection.cursor() as cursor:
            cursor.execute(
                "select topic_url url, topic_status status from TOPIC_INFO where %s TOPIC_STATUS='%s' limit %s" % (
                    fid_segment, status, num))
            result = cursor.fetchall()
        return result

    def get_file_url(self, num, target, fid_list=None):
        if isinstance(num, int):
            num = str(num)
        fid_segment = self.__build_fid_segment(fid_list)
        sql = """
            select ti.TOPIC_URL topicUrl,b.%s_URL fileUrl
                from TOPIC_INFO ti
                left join %s_INFO b
                 on (ti.TOPIC_URL = b.TOPIC_URL)
                    where %s ti.TOPIC_STATUS = '1'
                      and b.%s_STATUS = '0' limit %s
            """
        with self.connection.cursor() as cursor:
            cursor.execute(sql % (target, target, fid_segment, target, num))
            result = cursor.fetchall()
        return result

    def write_back_file_info(self, target, topic_url, file_url, file_path, file_id, file_status):
        sql = """
            update %s_INFO set %s_STATUS='%s' ,FILE_PATH='%s',FILE_ID='%s'
            where TOPIC_URL='%s' and %s_URL='%s'
        """ % (target, target, file_status, file_path, file_id, topic_url, target, file_url)
        try:
            with self.connection.cursor() as cursor:
                cursor.execute(sql)
                self.connection.commit()
        except Exception as e:
            self.logger.info(sql)
            self.logger.error("file url write db failed: %s", file_url)
            self.connection.rollback()


class Redis(LoggerObject):
    def __init__(self):
        super().__init__("mysql")
        redis.Redis(host='192.168.0.110', port=6379, db=0)
