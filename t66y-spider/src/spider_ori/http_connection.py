from urllib3 import PoolManager


class HttpConnection(object):

    def __init__(self, http_con_pool=None):
        if http_con_pool is None:
            http_con_pool = PoolManager(10)
        self.__http_con_pool = http_con_pool
        self.__headers = {
            "Connection": "keep-alive",
            "Cache-Control": "max-age=0",
            "Upgrade-Insecure-Requests": 1,
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
            "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding": "gzip, deflate",
            "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
        }

    def get(self, url, charset='gbk'):
        response = self.__http_con_pool.request("get", url, headers=self.__headers)
        return response.data.decode(charset)

    def post(self, url):
        pass

    @property
    def http_con_pool(self):
        return self.__http_con_pool

    @http_con_pool.setter
    def http_con_pool(self, http_con_pool):
        self.__http_con_pool = http_con_pool


