import math

import pymysql

headers = {
    "Connection": "keep-alive",
    "Cache-Control": "max-age=0",
    "Upgrade-Insecure-Requests": 1,
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate",
    "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
    # , "Cookie": "PHPSESSID=3q23r1ceckr5p1h84n2c023hg5"
}

mysql = {
    "host": "172.28.0.4",
    "port": 3306,
    "user": "appdata",
    "password": "123456",
    "db": "t66y",
    "charset": "utf8",
    "cursorclass": pymysql.cursors.DictCursor,
    "use_unicode": True
}

config_topic = {}
config_page = \
    {
        "target": "page",
        "threadNum": "1",
        "fidList": ["2"],
        "baseUrl": "www.t66y.com",
        "headers": headers,
        "mysqlConfig": mysql
    }


class Test(object):
    def __init__(self):
        self.__page_list = list(range(1, 101))

    def get_page_range(self, first_page_num, last_page_num):
        if first_page_num == last_page_num:
            return first_page_num
        middle_page_num = first_page_num + math.ceil(((last_page_num - first_page_num) + 1.0) / 2.0) - 1
        if self.is_handle(middle_page_num):
            # page deal
            return self.get_page_range(first_page_num, middle_page_num)
        else:
            return self.get_page_range(middle_page_num + 1, last_page_num)

        # self.__page_list = list(range(0, math.floor(((last_page_num - first_page_num) + 1.0) / 4.0)))

    def is_handle(self, curpage):
        print(curpage)
        return curpage >= 4


if __name__ == '__main__':
    # res = Downloader(HtmlResponseHandler(),proxy=True,proxy_url="http://127.0.0.1:1080").get("www.baidu.com")
    # res = Downloader(HtmlResponseHandler()).get("www.t66y.com")
    # print(res)
    # spider = Spider(config_page)
    # spider.run()
    print(Test().get_page_range(1, 100))
    #
    # my = MySql(**mysql)
    # cache = Cache(my)
    # print(cache.is_contain_url("htm_data/5/1807/3201529.html"))
    # a = my.find_all_url()
    # print(getsizeof(a))
    # pool = redis.ConnectionPool(host='172.28.0.2', port=6379, db=9)
    # r = redis.Redis(connection_pool=pool)
    # r.hset("urls", "fff", "aaaaa")
    # r.mset({"aa":2,"bb":"1"})
    # print(r.exists("aa","bb"))
    #
    # print(r.get('aaa'))
