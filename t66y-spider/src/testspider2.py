import multiprocessing
import os
from concurrent.futures import ThreadPoolExecutor

from urllib3 import PoolManager

from spider.parser import Parser
from spider.spider import Spider
from spider2 import base_url, page_path, headers


def run(url, fields):
    _pool = PoolManager(10)
    _response = _pool.request("get", url, fields=fields, headers=headers)
    _page_html = _response.data.decode("gbk")
    _topic_list = Parser.topic_from_page(_page_html)
    _spider = Spider()
    with ThreadPoolExecutor(10) as executor:
        all_task = [executor.submit(_spider.handle_topic, topic) for topic in _topic_list]
    for task in all_task:
        print(task)


if __name__ == '__main__':
    path = os.path.join(base_url, page_path)
    pool = multiprocessing.Pool(9)

    pool.apply_async(run, args=(path, {"fid": 15, "page": 2}))
    pool.apply_async(run, args=(path, {"fid": 15, "page": 3}))

    pool.close()
    pool.join()
