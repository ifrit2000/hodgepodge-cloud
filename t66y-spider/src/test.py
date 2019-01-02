# import shelve
#
# import pymysql
# from urllib3 import PoolManager
#
# import db
# import parser
# import spider
#
#
# def get_html(url):
#     pool_manager = PoolManager()
#     headers = {
#         "Connection": "keep-alive",
#         "Cache-Control": "max-age=0",
#         "Upgrade-Insecure-Requests": 1,
#         "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
#         "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
#         "Accept-Encoding": "gzip, deflate",
#         "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
#     }
#     res = pool_manager.request('GET', url, headers=headers)
#
#     return res.data.decode('gbk')
#
#
# def get():
#     page = get_html("http://www.t66y.com/thread0806.php?fid=2&page=2")
#     topic_list = parser.Parser.topic_from_page(page)
#     for topic in topic_list:
#         print("handle %s" % topic.title)
#         topic_html = get_html("http://www.t66y.com/" + topic.url)
#         parser.Parser.element_from_topic(topic, topic_html)
#         topic.area = '2'
#     s = shelve.open("test.shelve")
#     s["test"] = topic_list
#     s.close()
#
#
# def t():
#     s = shelve.open("test.shelve")
#     topic_list = s["test"]
#     s.close()
#     return topic_list
#
#
# def insert(topics):
#     connection = pymysql.connect(host="172.28.0.4", user="appopr", password="123456", database="service_cipher"
#                                  , use_unicode=True, charset='utf8', cursorclass=pymysql.cursors.DictCursor)
#     mysql = db.MySql(connection)
#     mysql.insert_topic(topics)
#
#
# if __name__ == "__main__":
#
#     ss=spider.Spider()
#     l=ss.run("http://t66y.com/thread0806.php?fid=15&search=&page=2")
#     s = shelve.open("test.shelve")
#     s["test"] = l
#     s.close()
#
