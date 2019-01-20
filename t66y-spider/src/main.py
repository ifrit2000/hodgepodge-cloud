from spider import task

if __name__ == '__main__':
    fid_dict = {
        "2": "亞洲無碼原創區",
        "15": "亞洲有碼原創區",
        "4": "歐美原創區",
        "25": "國產原創區",
        "5": "動漫原創區",
        "26": "中字原創區",
        "27": "轉帖交流區"
    }
    # daemon = Daemon(task=task, task_args=["2"])
    # daemon = Daemon(task=task, task_args=fid_dict.keys())
    # daemon.start()
    # response = urllib3.PoolManager(1).request("get", "https://hh.flexui.win/htm_data/4/1810/3314114.html", headers={
    #     "Connection": "keep-alive",
    #     "Cache-Control": "max-age=0",
    #     "Upgrade-Insecure-Requests": 1,
    #     "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
    #     "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    #     "Accept-Encoding": "gzip, deflate",
    #     "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"  # ,
    #     #  "Cookie": "PHPSESSID=3q23r1ceckr54p1h84n2c03hg5"
    #     # "Cookie": "227c9_lastvisit=0%091546770610%09%2Fnotice.php%3F"
    #     # "Cookie": "227c9_lastvisit=0%091546770610%09%2Fnotice.php%3F; __cfduid=d60fe4eb7497e25ac46c3347aa851e7871546770292"
    #     # "Cookie": "__cfduid=d0b366b14290cf500a30a32f3795a55c31546769053; PHPSESSID=lbt215f5ilp6hdmn0bs2nn49l5; serverInfo=www.t66y.com%7C45.62.117.196; 227c9_lastvisit=0%091546769149%09%2Fnotice.php%3F"
    # })
    # print(response.status)

    # print(response.data.decode('gbk'))
    for fid in fid_dict.keys():
        task(fid)
    # task("27")

    # with shelve.open("27") as db:
    #     print(db["1"])
