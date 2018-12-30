from bs4 import BeautifulSoup
from urllib3 import PoolManager
import time
import asyncio

def get_html(url):
    pool_manager=PoolManager()
    headers={
        "Host": "www.t66y.com",
"Connection": "keep-alive",
"Cache-Control": "max-age=0",
"Upgrade-Insecure-Requests": 1,
"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
"Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding": "gzip, deflate",
"Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
    }
    res = pool_manager.request('GET', url,headers=headers)
    return res.data.decode('gbk')

def parsePage(page):
    topicDict = {}
    soup=BeautifulSoup(page,"html.parser")
   
    table=soup.find(name="table",id="ajaxtable")
    #判断是否首页
    topic_start_flag=table.find_all(name="tr",attrs={"class":"tr2"})
    topic_start_flag=topic_start_flag[len(topic_start_flag)-1]
    for tr in topic_start_flag.find_next_siblings(name="tr",attrs={"class":"tr3 t_one tac"}):
        a=tr.find(name="h3").a
        topicDict[a['href']]=a.text
    return topicDict

def imageFilter(image):
    imageUrl=image['data-src']
    if imageUrl.endswith(".jpg"):
        return True
    else:
        return False

def parseTopic(topic):
    soup=BeautifulSoup(topic,"html.parser")
    images=list(map(lambda image:image['data-src'].replace(".th",""),filter(imageFilter, soup.select(".tpc_content img"))))
    torrentLinks =list(map(lambda a: a.text,filter(lambda a:"hash=" in a.text,soup.select(".tpc_content a"))))
    return images,torrentLinks

if __name__ == "__main__":
    start = time.perf_counter()
    page=get_html("http://www.t66y.com/thread0806.php?fid=2&page=2")
    dic=parsePage(page)
    topicList=[]
    for url,title in dic.items():
        topic=get_html("http://www.t66y.com/"+url)
        # images,torrentLinks=parseTopic(topic)
        # print(torrentLinks)
        topicList.append(topic)
    
    # topic=get_html("http://www.t66y.com/htm_data/15/1812/3385482.html")
    # parseTopic(topic)
    print(len(topicList))
    middle = time.perf_counter()
    for topic in topicList:
        parseTopic(topic)
    # print(start)
    # get_html("http://www.t66y.com/htm_data/15/1812/3385482.html")

    end = time.perf_counter()
    print(middle-start) 
    print(end-middle)