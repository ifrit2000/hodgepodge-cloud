import json

import pymysql

from spider import Spider

headers = {
    "Connection": "keep-alive",
    "Cache-Control": "max-age=0",
    "Upgrade-Insecure-Requests": 1,
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate",
    "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
    , "Cookie": "PHPSESSID=v2vvf8cuacn11kj1vpqep0v1u6"
    # , "Cookie": "PHPSESSID=0mhve2301bvbbbm1naqq61f9a7"
}

mysqlConfig = {
    "host": "172.28.0.4",
    "port": 3306,
    "user": "appdata",
    "password": "123456",
    "db": "t66y",
    "charset": "utf8",
    "cursorclass": pymysql.cursors.DictCursor,
    "use_unicode": True
}

config_topic = \
    {
        "target": "topic",
        "threadNum": 3,
        # "baseUrl": "hs.dety.men",
        "baseUrl": "www.t66y.com",
        "headers": headers,
        "mysqlConfig": mysqlConfig,
        "batchCount": 10,
        "fidList": ["26", "27", "25", "4", "2", "5", "15"]
    }

if __name__ == '__main__':
    spider = Spider(config_topic)
    spider.run()
