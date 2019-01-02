import multiprocessing

import urllib3


class Test:

    def __init__(self,http):
        self.__con = http

    @property
    def con(self):
        return self.__con

    @con.setter
    def con(self, http):
        self.__con = http

    def get(self, url):
        res = self.__con.request("get", url)
        print(res.status)




def get(url):
    test=Test(urllib3.PoolManager(2))
    test.get(url)


if __name__ == '__main__':
    pool = multiprocessing.Pool()

    pool.apply_async(get, args=("http://www.baidu.com",))
    pool.close()
    pool.join()
