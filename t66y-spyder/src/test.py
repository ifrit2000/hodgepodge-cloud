from bs4 import BeautifulSoup
from urllib3 import PoolManager

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
    print(res.status)
    return res.data.decode('gbk')

def parsePage(page):
    topicDict = {}
    soup=BeautifulSoup(page,"html.parser")
    soup.fetchNextSiblings
    table=soup.find(name="table",id="ajaxtable")
    #判断是否首页
    topic_start_flag=table.find_all(name="tr",attrs={"class":"tr2"})
    topic_start_flag=topic_start_flag[len(topic_start_flag)-1]
    for tr in topic_start_flag.find_next_siblings(name="tr",attrs={"class":"tr3 t_one tac"}):
        a=tr.find(name="h3").a
        topicDict[a['href']]=a.text
    return topicDict

def parseTopic(topic):
    soup=BeautifulSoup(post_content,"html.parser")
    print(type(soup.h4))
    title=soup.h4.string
    print(title.string)
    images=soup.select(".tpc_content img")
    print(len(images))
    print(images[30].attrs['data-src'])
    a=soup.select(".tpc_content a")
    b=a[len(a)-1]
    print(b.text)

def get_post():
    pool_manager=PoolManager()
    res = pool_manager.request('GET', 'http://www.t66y.com/htm_data/15/1812/3385482.html')
    return res.data.decode('gbk')

if __name__ == "__main__":
    #page=get_html("http://www.t66y.com/thread0806.php?fid=2&page=2")
    page=get_html("http://www.t66y.com/thread0806.php?fid=2")
    dic=parsePage(page)

    # post_content=get_post()
    # soup=BeautifulSoup(post_content,"html.parser")
    # print(type(soup.h4))
    # title=soup.h4.string
    # # print(title.string)
    # # images=soup.select(".tpc_content img")
    # # print(len(images))
    # # print(images[30].attrs['data-src'])
    # a=soup.select(".tpc_content a")
    # b=a[len(a)-1]
    # print(b.text)


