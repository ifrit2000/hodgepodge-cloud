import atexit
import errno
import logging
import multiprocessing
import os
import shelve
import signal
import sys
import time
from concurrent.futures import ThreadPoolExecutor, as_completed

import urllib3
# import spider.daemon
# import spider.db
# import spider.http_connection
# import spider.logger
# import spider.spider
from bs4 import BeautifulSoup

__all__ = ['daemon', 'db', 'http_connection', 'logger', 'spider']

__thread_count = 9
# __t66y_domain = "hh.flexui.win"
__t66y_domain_list = ["t66y.com", "hh.flexui.win"]
__t66y_domain = "t66y.com"
__t66y_page_path = "thread0806.php"
__fid = {
    "2": "亞洲無碼原創區",
    "15": "亞洲有碼原創區",
    "4": "歐美原創區",
    "25": "國產原創區",
    "5": "動漫原創區",
    "26": "中字原創區",
    "27": "轉帖交流區"
}
__page_num = list(range(1, 101))
__headers = {
    "Connection": "keep-alive",
    "Cache-Control": "max-age=0",
    "Upgrade-Insecure-Requests": 1,
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate",
    "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
}

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)


def parser_page_html(page_html):
    topic_list = []
    soup = BeautifulSoup(page_html, "html.parser")

    table = soup.find(name="table", id="ajaxtable")
    # 判断是否首页
    topic_start_flag = table.find_all(name="tr", attrs={"class": "tr2"})
    topic_start_flag = topic_start_flag[len(topic_start_flag) - 1]
    for tr in topic_start_flag.find_next_siblings(name="tr", attrs={"class": "tr3 t_one tac"}):
        a = tr.find(name="h3").a
        topic_list.append({"url": a['href'], "title": a.text})
    return topic_list


def parser_topic_html(topic):
    soup = BeautifulSoup(topic["html"], "html.parser")
    topic["images"] = list(
        map(lambda image: image['data-src'].replace(".th", ""),
            filter(lambda image: image['data-src'].endswith(".jpg"), soup.select(".tpc_content img"))))
    topic["torrent_links"] = list(
        map(lambda a: a.text, filter(lambda a: "hash=" in a.text, soup.select(".tpc_content a"))))
    return topic


def handle_area(fid):
    if fid not in fid.keys():
        # TODO log exception
        return
    for page_num in __page_num:
        handle_page(fid, page_num)


def handle_page(fid, page_num):
    http_pool = urllib3.PoolManager(__thread_count)
    page_url = os.path.join(__t66y_domain, __t66y_page_path)
    fields = {"fid": fid, "page": page_num}
    print("reqeust:", fields)
    response = http_pool.request("get", page_url, fields=fields, headers=__headers)
    if response.status != 200:
        # TODO log exception
        return list()
    print("request ok:")
    page_html = response.data.decode("gbk")
    topic_list = parser_page_html(page_html)
    with ThreadPoolExecutor(__thread_count) as executor:
        all_task = [executor.submit(handle_topic, topic, http_pool) for topic in topic_list]
    res = list()
    for future in as_completed(all_task):
        data = future.result()
        res.append(data)
    with shelve.open("%s.%s" % (fid, page_num)) as db:
        db["data"] = res


def handle_topic(topic, http_pool):
    page_url = os.path.join(__t66y_domain, topic["url"])
    response = http_pool.request("get", page_url, headers=__headers)
    if response.status != 200:
        topic["status"] = "get topic html failed"
        # TODO log exception
        return topic
    topic_html = response.data.decode("gbk")
    topic["html"] = topic_html
    return parser_topic_html(topic)


def check_domain():
    http_pool = urllib3.PoolManager(1)
    latencies = dict()
    for url in __t66y_domain_list:
        start = time.perf_counter()
        for i in range(0, 1):
            response = http_pool.request("get", url, timeout=urllib3.Timeout(connect=2.0, read=2.0), retries=False)
            if response.status != 200:
                latencies[url] = -1
            break
        cost = time.perf_counter() - start
        if url not in latencies.keys():
            latencies[url] = cost
    max_cost = sys.maxsize
    result = ""
    for (k, v) in filter(lambda item: item[1] > 0, latencies.items()):
        if v < max_cost:
            max_cost = v
            result = k
    print("choose:", result)
    return result


def get_logger(logger_name="default", filename='spider.log', log_path="/tmp/spider", level=logging.DEBUG):
    if not os.path.exists(log_path):
        os.mkdir(log_path)
    log_format = "%(asctime)s - [%(process)d] %(levelname)s: %(message)s ---(%(funcName)s@%(module)s:%(lineno)d)"
    date_format = "%Y-%m-%d %H:%M:%S"
    logger = logging.getLogger(logger_name)
    logger.setLevel(level)
    handlers = list()
    handlers.append(logging.StreamHandler(sys.stdout))
    handlers.append(logging.FileHandler(os.path.join(log_path, filename)))
    for handler in handlers:
        handler.setFormatter(logging.Formatter(fmt=log_format, datefmt=date_format))
        logger.addHandler(handler)
    return logger


class Daemon:
    __work_dir = '/tmp/spider'
    __logger = get_logger(logger_name="daemon", filename="daemon.log")

    def __init__(self, pid_file='spider.pid', stdin='/dev/null', stdout='/dev/null', stderr='/dev/null'):
        self.__pid_file = os.path.join(Daemon.work_dir(), pid_file)
        self.__stdin = stdin
        self.__stdout = stdout
        self.__stderr = stderr

        # create work dir
        if not os.path.exists(Daemon.work_dir()):
            os.mkdir(Daemon.work_dir())

    @staticmethod
    def logger():
        return Daemon.__logger

    @staticmethod
    def work_dir():
        return Daemon.__work_dir

    def __daemon(self):
        try:
            # 第一次fork，生成子进程，脱离父进程
            if os.fork() > 0:
                raise SystemExit(0)  # 退出主进程
        except OSError as e:
            self.__logger.error("fork #1 failed:\n")
            # sys.exit(1)
            raise RuntimeError('fork #1 faild: {0} ({1})\n'.format(e.errno, e.strerror))
        os.chdir(Daemon.work_dir())  # 修改工作目录
        os.setsid()  # 设置新的会话连接
        os.umask(0)  # 重新设置文件创建权限
        try:
            # 第二次fork，禁止进程打开终端
            if os.fork() > 0:
                raise SystemExit(0)
        except OSError as e:
            self.__logger.error("fork #2 failed:\n")
            # sys.exit(1)
            raise RuntimeError('fork #2 faild: {0} ({1})\n'.format(e.errno, e.strerror))

        # Flush I/O buffers
        sys.stdout.flush()
        sys.stderr.flush()

        # Replace file descriptors for stdin, stdout, and stderr
        with open(self.__stdin, 'rb', 0) as f:
            os.dup2(f.fileno(), sys.stdin.fileno())
        with open(self.__stdout, 'ab', 0) as f:
            os.dup2(f.fileno(), sys.stdout.fileno())
        with open(self.__stderr, 'ab', 0) as f:
            os.dup2(f.fileno(), sys.stderr.fileno())
        # Write the PID file
        with open(self.__pid_file, 'w') as f:
            print(os.getpid(), file=f)

        # Arrange to have the PID file removed on exit/signal
        atexit.register(lambda: os.remove(self.__pid_file))

        signal.signal(signal.SIGTERM, self.__sigterm_handler)
        signal.signal(signal.SIGCHLD, self.__wait_child)
        return

    @staticmethod
    def __wait_child(sig_no, frame):
        Daemon.logger().info('receive SIGCHLD')
        try:
            while True:
                Daemon.logger().info('__wait_child')
                # -1 表示任意子进程
                # os.WNOHANG 表示如果没有可用的需要 wait 退出状态的子进程，立即返回不阻塞
                c_pid, status = os.waitpid(-1, os.WNOHANG)
                if c_pid == 0:
                    Daemon.logger().info('no child process was immediately available')
                    break
                exitcode = status >> 8
                Daemon.logger().info('child process %s exit with exitcode %s', c_pid, exitcode)
        except OSError as e:
            if e.errno == errno.ECHILD:
                Daemon.logger().error(e)
                Daemon.logger().error('current process has no existing unwaited-for child processes.')
            else:
                Daemon.logger().error("unknown error")
                raise
        Daemon.logger().info('handle SIGCHLD end')

    @staticmethod
    def __sigterm_handler(sig_no, frame):
        Daemon.logger().info('end')
        raise SystemExit(1)

    def start(self):
        if os.path.exists(self.__pid_file):
            Daemon.logger().error("the deamon is already running!!!")
            return
        try:
            self.__daemon()
        except RuntimeError as e:
            Daemon.logger().error(e)
            raise SystemExit(1)
        self.run()

    def run(self):
        Daemon.logger().info("start")
        p = multiprocessing.Pool(5)
        for fid in ["2", "15"]:
            try:
                p.apply_async(handle_area, args=(fid,))
            except Exception as e:
                Daemon.logger().error(e)
            time.sleep(1)
        p.close()
        p.join()

    def stop(self):
        try:
            if os.path.exists(self.__pid_file):
                with open(self.__pid_file) as f:
                    os.kill(int(f.read()), signal.SIGTERM)
            else:
                Daemon.logger().info('Not running.', file=sys.stderr)
                raise SystemExit(1)
        except OSError as e:
            if 'No such process' in str(e) and os.path.exists(self.__pid_file):
                os.remove(self.__pid_file)

    def restart(self):
        self.stop()
        self.start()


if __name__ == '__main__':
    flag = True
    __t66y_domain = check_domain()
    # flag = False
    # if flag:
    #     start = time.perf_counter()
    #     handle_page("2", "2")
    #     handle_page("2", "3")
    #     handle_page("2", "4")
    #     handle_page("2", "5")
    #     print(time.perf_counter() - start)
    # else:
    #     response = urllib3.PoolManager(__thread_count).request("get",
    #                                                            "http://www.t66y.com/thread0806.php?fid=2&search=&page=2",
    #                                                            headers=__headers)
    #     print(response.status)
    daemon = Daemon()
    daemon.start()

# 5 39.55670711299899
# 10 23.800040684000123
# 20 23.917132747999858
