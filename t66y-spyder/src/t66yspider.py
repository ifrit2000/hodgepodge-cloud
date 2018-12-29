import urllib3


class T66ySpider(object):

    def __init__(self, request=urllib3.PoolManager()):
        self.__request = request

    @property
    def request(self):
        return self.__request

    @request.setter
    def request(self, request):
        self.__request = request

    def post(self):
        self.request.request('post', 'http://www.baidu.com')

    def get(self):
        response = self.request.request('get', 'http://www.baidu.com')
        print(response.status)


if __name__ == "__main__":
    spider = T66ySpider()
    spider2 = T66ySpider(urllib3.PoolManager())
    spider2.get()
