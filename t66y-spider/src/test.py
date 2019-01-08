import traceback

import pymysql
from bs4 import BeautifulSoup


def parser_topic_html(topic):
    soup = BeautifulSoup(topic["html"], "html.parser")
    if soup.select(".tpc_content img") is None:
        topic["status"] = "error"
        return topic
    try:
        get_image = lambda tag: list(
            map(lambda image: image[tag].replace(".th", ""),
                filter(lambda image: image.get(tag) is not None and (
                        image[tag].endswith(".jpg") or image[tag].endswith(".JPG") or image[tag].endswith(".jpeg")),
                       soup.select(".tpc_content img"))))
        topic["images"] = list(set(get_image("data-src")) | set(get_image("src")))
        topic["torrent_links"] = list(
            map(lambda a: a.text, filter(lambda a: "hash=" in a.text, soup.select(".tpc_content a"))))
        if len(topic.get("images")) == 0:
            print(soup.select(".tpc_content img"))
    except Exception as e:
        print(traceback.format_exc())
        topic["status"] = "image or torrent error"
    return topic


if __name__ == '__main__':
    con = pymysql.connect(host="172.28.0.4",
                          port=3306,
                          user='appdata',
                          password='123456',
                          db='t66y',
                          charset='utf8',
                          cursorclass=pymysql.cursors.DictCursor,
                          use_unicode=True)
    topic = {}
    with con.cursor() as cursor:
        # cursor.execute("select topic_url,html from HTML_INFO where topic_url='htm_data/5/1805/3136018.html'") #jpeg
        # cursor.execute("select topic_url,html from HTML_INFO where topic_url='htm_data/5/1807/3201522.html'") #JPG
        # cursor.execute("select topic_url,html from HTML_INFO where topic_url='htm_data/5/1804/3121659.html'") #png
        cursor.execute("select topic_url,html from HTML_INFO ")
        res = cursor.fetchall()
        for result in res:
            topic["html"] = result["html"]
            topic["url"] = result["topic_url"]
            parser_topic_html(topic)

    con.close()
