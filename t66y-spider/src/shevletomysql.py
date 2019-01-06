import pymysql


def insert_topic(connection, topics=None):
    if topics is None:
        return None
    with connection.cursor() as cursor:
        topic_sql_template = """insert into t66y.TOPIC_INFO(TOPIC_URL, TOPIC_AREA, TOPIC_TITLE) 
            VALUES('%s','%s','%s')"""
        images_sql_template = """insert into t66y.IMAGE_INFO(TOPIC_URL, IMAGE_URL) 
            VALUE %s"""
        torrent_sql_template = """insert into t66y.TORRENT_INFO(TOPIC_URL, TORRENT_URL, TORRENT_HASH) 
            VALUE %s"""

        for topic in topics:
            cursor.execute(topic_sql_template % (topic.get("url"), topic.area, topic.title))
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
            cursor.commit()


if __name__ == '__main__':
    connection = pymysql.connect(host='localhost',
                                 user='user',
                                 password='passwd',
                                 db='db',
                                 charset='utf8mb4',
                                 cursorclass=pymysql.cursors.DictCursor)
    a = dict()
    a.setdefault("url", 123)
    print(a.get("url"))

