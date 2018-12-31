from urllib3 import PoolManager

import parser


def get_html(url):
    pool_manager = PoolManager()
    headers = {
        "Connection": "keep-alive",
        "Cache-Control": "max-age=0",
        "Upgrade-Insecure-Requests": 1,
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "Accept-Encoding": "gzip, deflate",
        "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
    }
    res = pool_manager.request('GET', url, headers=headers)
    return res.data.decode('gbk')


if __name__ == "__main__":
    # start = time.perf_counter()
    page = get_html("http://www.t66y.com/thread0806.php?fid=2&page=2")
    topic_list = parser.Parser.topic_from_page(page)
    for topic in topic_list:
        topic_html = get_html("http://www.t66y.com/" + topic.url)
        parser.Parser.element_from_topic(topic, topic_html)
    for topic in topic_list:
        print(topic.torrent_links)
    # dic=parsePage(page)
    # topicList=[]
    # for url,title in dic.items():
    #     topic=get_html("http://www.t66y.com/"+url)
    #     # images,torrentLinks=parseTopic(topic)
    #     # print(torrentLinks)
    #     topicList.append(topic)
    #
    # # topic=get_html("http://www.t66y.com/htm_data/15/1812/3385482.html")
    # # parseTopic(topic)
    # print(len(topicList))
    # middle = time.perf_counter()
    # for topic in topicList:
    #     parseTopic(topic)
    # # print(start)
    # # get_html("http://www.t66y.com/htm_data/15/1812/3385482.html")
    #
    # end = time.perf_counter()
    # print(middle-start)
    # print(end-middle)
