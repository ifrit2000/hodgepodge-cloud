import urllib3

from log import LoggerObject

urllib3.disable_warnings()


class Downloader(LoggerObject):
    def __init__(self, response_processor=None, num_pools=10, **kw):
        super().__init__("downloader")
        if kw.get("proxy_url") is not None:
            self.__pool = urllib3.ProxyManager(num_pools=num_pools, proxy_url=kw["proxy_url"])
        else:
            self.__pool = urllib3.PoolManager(num_pools=num_pools)
        self.__headers = kw.get("headers", None)
        self.__response_processor = response_processor

    def __request(self, url, method, fields=None, headers=None, body=None, response_processor=None):
        if headers is None:
            headers = self.__headers
        response = self.__pool.request(method, url=url, fields=fields, headers=headers)
        if response_processor is None:
            return self.__response_processor.process(response)
        else:
            return response_processor.process(response)

    def get(self, url, fields=None, headers=None, response_processor=None):
        self.logger.debug("GET %s" % url)
        return self.__request(url=url, method="GET", fields=fields, headers=headers,
                              response_processor=response_processor)

    def post(self, url, fields=None, headers=None, body=None, response_processor=None):
        self.logger.debug("POST %s" % url)
        return self.__request(url=url, method="POST", fields=fields, headers=headers, body=body,
                              response_processor=response_processor)

    @property
    def response_processor(self):
        return self.__response_processor

    @response_processor.setter
    def response_processor(self, response_processor):
        self.__response_processor = response_processor


class ResponseProcessor(LoggerObject):

    def __init__(self):
        super().__init__("responseProcess")

    def process(self, response):
        if response is None:
            self.logger.error("%s response is None" % response.geturl())
            return None
        if response.status != 200:
            self.logger.error("%s response code: %s" % (response.geturl(), str(response.status)))
            return None
        return self._process(response)

    def _process(self, response):
        pass


class HtmlResponseProcessor(ResponseProcessor):

    def __init__(self, charset="gbk"):
        super().__init__()
        self.__charset = charset

    def _process(self, response):
        return response.data.decode(self.__charset, "ignore")


class FileResponseProcessor(ResponseProcessor):

    def _process(self, response):
        return response.data
