import time
from concurrent.futures import ThreadPoolExecutor,as_completed

import urllib3

__count = 20


def test(http):
    res = http.request("get", "http://www.baidu.com")
    return res.status


def multithread(http):
    with ThreadPoolExecutor(3) as executor:
        all_task = [executor.submit(test, http) for i in range(0, __count)]

    for future in as_completed(all_task):
        data = future.result()
        print("in main: get page {}s success".format(data))

def singletread(http):
    for i in range(0, __count):
        test(http)


if __name__ == '__main__':
    http = urllib3.PoolManager(10)
    http2 = urllib3.PoolManager(10)
    start = time.perf_counter()
    multithread(http)
    # singletread(http2)
    end = time.perf_counter()

    print(end - start)
