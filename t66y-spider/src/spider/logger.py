import logging
from sys import stdout


def get_logger(logger_name="child", filename='./spider.log', level=logging.DEBUG):
    log_format = "%(asctime)s - [%(process)d] %(levelname)s: %(message)s ---(%(funcName)s@%(module)s:%(lineno)d)"
    date_format = "%Y-%m-%d %H:%M:%S"
    logger = logging.getLogger(logger_name)
    logger.setLevel(level)
    handlers = list()
    handlers.append(logging.StreamHandler(stdout))
    handlers.append(logging.FileHandler(filename))
    for handler in handlers:
        handler.setFormatter(logging.Formatter(fmt=log_format, datefmt=date_format))
        logger.addHandler(handler)
    return logger
