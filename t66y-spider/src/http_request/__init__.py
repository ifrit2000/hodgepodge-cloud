import urllib3

from log import LoggerObject


class Downloader(LoggerObject):
    def __init__(self, response_handler, num_pools=10, **kw):
        super().__init__("downloader")
        if kw.get("proxy_url") is not None:
            self.__pool = urllib3.ProxyManager(num_pools=num_pools, proxy_url=kw["proxy_url"])
        else:
            self.__pool = urllib3.PoolManager(num_pools=num_pools)
        self.__headers = kw.get("headers", None)
        self.__response_handler = response_handler

    def __request(self, url, method, fields=None, headers=None, body=None):
        if headers is None:
            headers = self.__headers
        response = self.__pool.request(method, url=url, fields=fields, headers=headers, body=body)
        return self.__response_handler.handle(response)

    def get(self, url, fields=None, headers=None):
        self.logger.debug("GET %s" % url)
        return self.__request(url=url, method="GET", fields=fields, headers=headers)

    def post(self, url, fields=None, headers=None, body=None):
        self.logger.debug("POST %s" % url)
        return self.__request(url=url, method="POST", fields=fields, headers=headers, body=body)


class ResponseProcessor(LoggerObject):

    def __init__(self):
        super().__init__("responseProcess")

    def handle(self, response):
        return self._handle(response)

    def _handle(self, response):
        pass


class HtmlResponseProcessor(ResponseProcessor):

    def _handle(self, response):
        if response is None:
            self.logger.error("response is None")
            return None
        if response.status != 200:
            self.logger.error("response code: %s" % str(response.status))
            return None
        self.logger.debug("OK %s" % response.geturl())
        return response.data.decode("gbk", "ignore")
