import logging


def get_logger(logger_name="child", filename='./spider.log', level=logging.DEBUG):
    log_format = "%(asctime)s - [%(process)d] %(levelname)s: %(message)s ---(%(funcName)s@%(module)s:%(lineno)d)"
    date_format = "%Y-%m-%d %H:%M:%S"
    logging.basicConfig(filename=filename, level=level, format=log_format, datefmt=date_format)
    logger = logging.Logger(name=logger_name, level=level)
    logger.parent = logging.root
    return logger
