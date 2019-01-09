from handler import HtmlResponseHandler
from http_request import Downloader

if __name__ == '__main__':
    # res = Downloader(HtmlResponseHandler(),proxy=True,proxy_url="http://127.0.0.1:1080").get("www.baidu.com")
    res = Downloader(HtmlResponseHandler()).get("www.t66y.com")
    print(res)
