import getopt
import json
import sys

import pymysql

from spider import Spider

fid_dict = {
    "2": "亞洲無碼原創區",
    "15": "亞洲有碼原創區",
    "4": "歐美原創區",
    "25": "國產原創區",
    "5": "動漫原創區",
    "26": "中字原創區",
    "27": "轉帖交流區"
}


def usage():
    print("usage: python package [OPTIONS]")
    print("Options:")
    print(" -t, --target")
    print(" -c, --config")
    print("     --thread-num        default ")
    print("     --base-url          default ")
    print("     --fid-list          default")
    print("     --batch-count       default")
    print("     --file-path         default")
    print(" -s, --t66y-session-id   default")
    print("     --config-file")
    print("     --redis-host")
    print("     --redis-port        default")
    print("     --redis-db")
    print("     --mysql-host")
    print("     --mysql-port        default")
    print("     --mysql-user")
    print("     --mysql-password")
    print("     --mysql-db")
    print("     --mysql-charset     default")

    sys.exit(0)


def get_config(_opts):
    _headers = {
        "Connection": "keep-alive",
        "Cache-Control": "max-age=0",
        "Upgrade-Insecure-Requests": 1,
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "Accept-Encoding": "gzip, deflate",
        "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"
    }
    _default_redis_config = {
        "port": 6379,
        "db": 0
    }
    _default_mysql_config = {
        "cursorclass": pymysql.cursors.DictCursor,
        "use_unicode": True,
        "port": 3306,
        "charset": "utf8"
    }
    _default_config = {
        "threadNum": 1,
        "fidList": ["2", "4", "5", "15", "25", "26", "27"],
        "baseUrl": "www.t66y.com",
        "batchCount": 15
    }
    _cmd_config = dict()
    _cmd_redis_config = dict()
    _cmd_mysql_config = dict()
    _file_config = None
    _t66y_session_id = None
    for opt, value in _opts:
        if opt == '-h' or opt == '--help':
            usage()
            sys.exit(0)
        if opt == '-c' or opt == '--config-file':
            with open(value, 'r') as file:
                _file_config = json.load(file)
        if opt == '--target' or opt == '-t':
            _cmd_config["target"] = value
        if opt == '--thread-num':
            _cmd_config["threadNum"] = int(value)
        if opt == '--base-url':
            _cmd_config["baseUrl"] = int(value)
        if opt == '--fid-list':
            _cmd_config["fidList"] = value.split(",")
        if opt == '--redis-host':
            _cmd_redis_config["host"] = value
        if opt == '--redis-port':
            _cmd_redis_config["port"] = int(value)
        if opt == '--redis-db':
            _cmd_redis_config["db"] = int(value)
        if opt == '--mysql-host':
            _cmd_mysql_config["host"] = value
        if opt == '--mysql-port':
            _cmd_mysql_config["port"] = int(value)
        if opt == '--mysql-user':
            _cmd_mysql_config["user"] = value
        if opt == '--mysql-password':
            _cmd_mysql_config["password"] = value
        if opt == '--mysql-db':
            _cmd_mysql_config["db"] = value
        if opt == '--mysql-charset':
            _cmd_mysql_config["charset"] = value
        if opt == '--batch-count':
            _cmd_config["batchCount"] = value
        if opt == '--file-path':
            _cmd_config["filePath"] = value
        if opt == '--t66y-session-id' or opt == '-s':
            _t66y_session_id = "PHPSESSID=" + value
    _default_config.update(_cmd_config)
    _default_mysql_config.update(_cmd_mysql_config)
    _default_redis_config.update(_cmd_redis_config)

    if _file_config is not None:
        _file_mysql_config = _file_config.get("mysqlConfig", dict())
        _file_redis_config = _file_config.get("redisConfig", dict())
        _file_config.update(_cmd_config)
        _file_mysql_config.update(_cmd_mysql_config)
        _file_redis_config.update(_cmd_redis_config)
        _cmd_config = _file_config
        _cmd_mysql_config = _file_mysql_config
        _cmd_redis_config = _file_redis_config

    _default_config.update(_cmd_config)
    _default_mysql_config.update(_cmd_mysql_config)
    _default_redis_config.update(_cmd_redis_config)
    if _default_config.get("target") in ["page", "topic"] and _t66y_session_id is not None:
        _headers["Cookie"] = _t66y_session_id
    _default_config["headers"] = _headers
    _default_config["mysqlConfig"] = _default_mysql_config
    _default_config["redisConfig"] = _default_redis_config

    return _default_config


def check_config(_config):
    _error_list = list()
    if _config.get("target") is None:
        _error_list.append("参数target不能为空")
    err_fid = list(filter(lambda fid: fid not in ["2", "4", "5", "15", "25", "26", "27"], _config.get("fidList")))
    if len(err_fid) != 0:
        _error_list.append(",".join(err_fid) + "不在列表中")

    if _config.get("mysqlConfig").get("host") is None:
        _error_list.append("参数mysql-host不能为空")
    if _config.get("mysqlConfig").get("user") is None:
        _error_list.append("参数mysql-user不能为空")
    if _config.get("mysqlConfig").get("db") is None:
        _error_list.append("参数mysql-db不能为空")
    if _config.get("mysqlConfig").get("password") is None:
        _error_list.append("参数mysql-password不能为空")

    if _config.get("target") == "page":
        if _config.get("redisConfig").get("host") is None:
            _error_list.append("参数redis-host不能为空")

    if _config.get("target") in ["image", "torrent"]:
        if _config.get("filePath") is None:
            _error_list.append("参数file-path不能为空")
    return _error_list


def run():
    short_opts = "hc:t:s"
    long_opts = ["help", "target=", "thread-num=", "base-url=", "fid-list=",
                 "redis-host=", "redis-port=", "redis-db=", "mysql-host=",
                 "mysql-port=", "mysql-user=", "mysql-password=", "mysql-db=",
                 "mysql-charset=", "batch-count=", "file-path=",
                 "t66y-session-id=", "config-file="]
    opts = None
    try:
        opts, args = getopt.getopt(sys.argv[1:], short_opts, long_opts)
    except getopt.GetoptError as e:
        print(e)
        usage()
    config = get_config(opts)
    error_list = check_config(config)
    if len(error_list) != 0:
        for err in error_list:
            print(err)
        usage()
    spider = Spider(config)
    spider.run()


if __name__ == '__main__':
    run()
