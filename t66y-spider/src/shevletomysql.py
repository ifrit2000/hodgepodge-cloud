import os
import shelve
import traceback

import pymysql


def insert_topic(connection, topic_list=None):
    if topic_list is None:
        return None
    topic_sql_template = """insert into t66y.TOPIC_INFO(TOPIC_URL, TOPIC_AREA, TOPIC_TITLE, TOPIC_STATUS) 
            VALUES('%s','%s','%s','%s')"""
    images_sql_template = """insert into t66y.IMAGE_INFO(TOPIC_URL, IMAGE_URL) 
            VALUE %s"""
    torrent_sql_template = """insert into t66y.TORRENT_INFO(TOPIC_URL, TORRENT_URL, TORRENT_HASH) 
            VALUE %s"""
    html_sql_template = """insert into t66y.HTML_INFO(TOPIC_URL, HTML) 
            VALUES('%s','%s')"""
    with connection.cursor() as cursor:
        for topic in topic_list:
            # print(topic_sql_template % (topic.get("url"), topic.get("area"), topic.get("title")))
            cursor.execute(
                topic_sql_template % (topic.get("url"), topic.get("area"), topic.get("title"), topic.get("status")))
            image_value = ""
            for image in topic.get("images", []):
                image_value = image_value + "('%s','%s')," % (topic.get("url"), image)
            if image_value != "":
                sql = (images_sql_template % image_value).rstrip(",")
                cursor.execute(sql)

            torrent_value = ""
            for torrent_link in topic.get("torrent_links", []):
                torrent_hash = torrent_link[torrent_link.find("hash=") + len("hash="):]
                torrent_value = torrent_value + "('%s','%s','%s')," % (topic.get("url"), torrent_link, torrent_hash)
            if torrent_value != "":
                sql = (torrent_sql_template % torrent_value).rstrip(",")
                cursor.execute(sql)
            if topic["status"] != "0" and topic["html"] is not None:
                sql = html_sql_template % (topic.get("url"), pymysql.escape_string(topic.get("html")))
                cursor.execute(sql)


if __name__ == '__main__':
    con = pymysql.connect(host="172.28.0.4",
                          port=3306,
                          user='appdata',
                          password='123456',
                          db='t66y',
                          charset='utf8',
                          cursorclass=pymysql.cursors.DictCursor,
                          use_unicode=True)
    topic_dict = {}
    try:
        for fid in ["2", "4", "5", "15", "25", "26", "27"]:
            with shelve.open(os.path.join("/home/anthony/Desktop/data", fid)) as db:
                for page_num in range(1, 101):
                    topic_list = db.get(str(page_num), [])
                    for topic in topic_list:
                        topic["area"] = fid
                        if "'" in topic["title"]:
                            topic["title"] = pymysql.escape_string(topic["title"])
                        topic_dict[topic["url"]] = topic
                        if topic.get("status", "0") == "0":
                            topic["status"] = "0"
                            topic["html"] = None
                        else:
                            topic["status"] = "1"
            print("fid %s load completed" % fid)
        # with shelve.open(os.path.join("/home/anthony/Desktop/data", "all")) as db2:
        #     db2["data"] = topic_dict
        print("write db")
        insert_topic(con, list(topic_dict.values()))
        print("finish write")
        con.commit()
        print("committed")
    except Exception as e:
        con.rollback()
        print(traceback.format_exc())
        pass
    finally:
        con.close()
