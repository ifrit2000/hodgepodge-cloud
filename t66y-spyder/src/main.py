import urllib3

http = urllib3.PoolManager()

res = http.request('GET', 'http://www.baidu.com')

res.status


def test():
    print(112233)
    return "ssssss"
