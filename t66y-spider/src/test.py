import urllib3

from http_request import FileResponseProcessor

if __name__ == '__main__':
    http = urllib3.PoolManager()
    response = http.request("get",
                            "http://n.sinaimg.cn/sports/2_img/upload/69e00db4/213/w2048h1365/20190114/t0_P-hrpcmqw7448431.jpg")
    processor = FileResponseProcessor()
    image = processor.handle(response)
    print(len(image) / 1024/1024)
