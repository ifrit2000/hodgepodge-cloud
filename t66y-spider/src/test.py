import traceback

import pymysql
from bs4 import BeautifulSoup


def parser_topic_html(topic):
    soup = BeautifulSoup(topic["html"], "html.parser")
    topic["status"] = "1"
    if soup.select(".tpc_content img") is None:
        topic["status"] = "error"
        return topic
    try:
        get_image = lambda tag: list(
            map(lambda image: image[tag].replace(".th", ""),
                filter(lambda image: image.get(tag) is not None and (
                        image[tag].endswith(".jpg") or image[tag].endswith(".JPG") or image[tag].endswith(".jpeg") or
                        image[tag].endswith(".png")),
                       soup.select(".tpc_content img"))))
        topic["images"] = list(set(get_image("data-src")) | set(get_image("src")))
        topic["torrent_links"] = list(
            map(lambda a: a.text, filter(lambda a: "hash=" in a.text, soup.select(".tpc_content a"))))
        if len(topic.get("images")) == 0:
            topic["status"] = None
    except Exception as e:
        print(traceback.format_exc())
        topic["status"] = "image or torrent error"
    return topic


topic_sql_template = """insert into t66y.TOPIC_INFO(TOPIC_URL, TOPIC_AREA, TOPIC_TITLE, TOPIC_STATUS) 
          VALUES('%s','%s','%s','%s')"""
images_sql_template = """insert into t66y.IMAGE_INFO(TOPIC_URL, IMAGE_URL) 
          VALUE %s"""
torrent_sql_template = """insert into t66y.TORRENT_INFO(TOPIC_URL, TORRENT_URL, TORRENT_HASH) 
          VALUE %s"""


def update_topic(cursor, topic):
    if topic.get("status", True):
        image_value = ""
        for image in topic.get("images", []):
            image_value = image_value + "('%s','%s')," % (topic.get("url"), image)
        if image_value != "":
            sql = (images_sql_template % image_value).rstrip(",")
            cursor.execute(sql)

        torrent_value = ""
        for torrent_link in topic.get("torrent_links", []):
            torrent_hash = torrent_link[torrent_link.find("hash=") + len("hash="):]
            torrent_value = torrent_value + "('%s','%s','%s')," % (
                topic.get("url"), torrent_link, torrent_hash)
        if torrent_value != "":
            sql = (torrent_sql_template % torrent_value).rstrip(",")
            cursor.execute(sql)
        cursor.execute("delete from HTML_INFO where topic_url='%s'" % topic["url"])


if __name__ == '__main__':
    con = pymysql.connect(host="172.28.0.4",
                          port=3306,
                          user='appdata',
                          password='123456',
                          db='t66y',
                          charset='utf8',
                          cursorclass=pymysql.cursors.DictCursor,
                          use_unicode=True)

    try:
        with con.cursor() as cursor:
            pass
            # cursor.execute("select topic_url,html from HTML_INFO ")
            # res = cursor.fetchall()
            # for result in res:
            #     topic = dict()
            #     topic["html"] = result["html"]
            #     topic["url"] = result["topic_url"]
            #     parser_topic_html(topic)
            #     update_topic(cursor, topic)
            # con.commit()
            topic={'url': 'htm_data/2/1901/3398957.html', 'title': '[MP4]071317-008 縦型動画020 ～神秘的熟女～ 高嶋祥子 [VIP9920]'}
            cursor.execute(
                "select count(1) url_num from TOPIC_INFO where TOPIC_URL='%s'" % topic.get("url", '1'))
    except Exception as e:
        con.rollback()
    finally:
        con.close()
