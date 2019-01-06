
class MySql(object):

    def __init__(self, connection=None):
        self.__connection = connection

    @property
    def connection(self):
        return self.__connection

    @connection.setter
    def connection(self, connection):
        self.__connection = connection

    def insert_topic(self, topics=None):
        if topics is None:
            return None
        cursor = self.__connection.cursor()
        topic_sql_template = """insert into t66y.TOPIC_INFO(TOPIC_URL, TOPIC_AREA, TOPIC_TITLE) 
        VALUES('%s','%s','%s')"""
        images_sql_template = """insert into t66y.IMAGE_INFO(TOPIC_URL, IMAGE_URL) 
        VALUE %s"""
        torrent_sql_template = """insert into t66y.TORRENT_INFO(TOPIC_URL, TORRENT_URL, TORRENT_HASH) 
        VALUE %s"""

        for topic in topics:
            cursor.execute(topic_sql_template % (topic.url, topic.area, topic.title))
            image_value = ""
            for image in topic.images:
                image_value = image_value + "('%s','%s')," % (topic.url, image)
            if image_value != "":
                sql = (images_sql_template % image_value).rstrip(",")
                cursor.execute(sql)

            torrent_value = ""
            for torrent_link in topic.torrent_links:
                torrent_hash = torrent_link[torrent_link.find("hash=") + len("hash="):]
                torrent_value = torrent_value + "('%s','%s','%s')," % (topic.url, torrent_link, torrent_hash)
            if torrent_value != "":
                sql = (torrent_sql_template % torrent_value).rstrip(",")
                cursor.execute(sql)

        self.__connection.commit()
        cursor.close()

    def close(self):
        self.__connection.close()


if __name__ == '__main__':
    # mysql = MySql()
    # sql = "insert into t66y.TOPIC_INFO(TOPIC_URL, TOPIC_PARTITION, TOPIC_TITLE) VALUE ('222','33','22')"
    #
    # mysql.insert(sql)
    # mysql.close()
    a = "http://www.rmdown.com/link.php?hash=1835b7f877f818f7122971610ac212649323ae65506"
    c = a[a.find("has1h=") + len("has1h="):]
    print(c)
    print(a)
