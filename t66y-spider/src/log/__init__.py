import logging
import os
import sys
import time


class Logger(object):
    def __init__(self, name, file, path='/tmp/log', level=logging.DEBUG):
        if not os.path.exists(path):
            os.mkdir(path)
        log_format = "%(asctime)s - [%(process)d] %(levelname)s: %(message)s ---(%(funcName)s@%(module)s:%(lineno)d)"
        date_format = "%Y-%m-%d %H:%M:%S"
        logger = logging.getLogger(name)
        logger.setLevel(level)
        handlers = list()
        handlers.append(logging.FileHandler(os.path.join(path, file)))
        handlers.append(logging.StreamHandler(sys.stdout))
        for handler in handlers:
            handler.setFormatter(logging.Formatter(fmt=log_format, datefmt=date_format))
            logger.addHandler(handler)
        self.__logger = logger

    def info(self, msg, *args, **kwargs):
        self.__logger.info(msg, *args, **kwargs)

    def debug(self, msg, *args, **kwargs):
        self.__logger.debug(msg, *args, **kwargs)

    def warning(self, msg, *args, **kwargs):
        self.__logger.warning(msg, *args, **kwargs)

    def error(self, msg, *args, **kwargs):
        self.__logger.error(msg, *args, exc_info=True, stack_info=True, **kwargs)


class LoggerObject(object):
    def __init__(self, name=None, file="spider.log"):
        if name is None:
            name = str(time.clock())
        self.__logger = Logger(name, file)

    @property
    def logger(self):
        return self.__logger
