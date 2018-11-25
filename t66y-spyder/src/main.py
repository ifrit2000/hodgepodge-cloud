import urllib3



http = urllib3.PoolManager()

res = http.request('GET', 'http://www.baidu.com')

print(res.data)


def test():
    return "ssssss"
