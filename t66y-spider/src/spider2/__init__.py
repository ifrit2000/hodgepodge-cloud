fid = {
    "2": "亞洲無碼原創區",
    "15": "亞洲有碼原創區",
    "4": "歐美原創區",
    "25": "國產原創區",
    "5": "動漫原創區",
    "26": "中字原創區",
    "27": "轉帖交流區"
}

base_url = "http://www.t66y.com"
page_path = "thread0806.php"
page_num = list(range(1, 101))
headers = {
    "Connection": "keep-alive",
    "Cache-Control": "max-age=0",
    "Upgrade-Insecure-Requests": 1,
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate",
    "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
}


class PageHandler(object):

    def __init__(self, con):
        self.__con = con
        pass

    def handle(self, html):
        self.__con.request("get", "http://www.baidu.com")
        print(html)
