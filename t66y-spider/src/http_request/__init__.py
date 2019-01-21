from urllib.parse import urlsplit

import urllib3

from log import LoggerObject

urllib3.disable_warnings()


class Downloader(LoggerObject):
    def __init__(self, response_processor=None, num_pools=10, **kw):
        super().__init__("downloader")
        if kw.get("proxy_url") is not None:
            print(11)
            self.logger.info("user proxy: %s" % kw.get("proxy_url"))
            self.__pool = urllib3.ProxyManager(num_pools=num_pools, proxy_url=kw["proxy_url"])
        else:
            self.__pool = urllib3.PoolManager(num_pools=num_pools, timeout=18, retries=3)
        self.__headers = kw.get("headers", None)
        self.__response_processor = response_processor
        self.__headers_cache = dict()

    def __request(self, url, method, fields=None, headers=None, body=None, response_processor=None):
        if headers is None:
            headers = self.__headers
        response = None
        try:
            _domain = urlsplit(url).netloc
            cookies = self.__headers_cache.get(_domain, dict())
            headers.update(cookies)
            response = self.__pool.request(method, url=url, fields=fields, headers=headers)
            response_headers = response.headers
            _cookie = None
            if "Set-Cookie" in response_headers:
                _cookie = response_headers.get("Set-Cookie").split(";")
            elif "set-cookie" in response_headers:
                _cookie = response_headers.get("Set-Cookie").split(";")
            if _cookie is None:
                _cookie = list()
            tmp_list = list(filter(lambda cookie: "PHPSESSID" in cookie, _cookie))
            if len(tmp_list) == 1:
                phpsessid = tmp_list[0][tmp_list[0].index("PHPSESSID"):]
                cookies.update({"Cookie": phpsessid})
            etag = response_headers.get("ETag")
            if etag is None:
                etag = response_headers.get("etag")
            if etag is not None:
                cookies.update({"If-None-Match": etag})
            self.__headers_cache.update({_domain: cookies})
        except Exception as e:
            self.logger.error("time out: %s", url)
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
