import os
import random
import time
from multiprocessing import Pool

import spider


def long_time_task(name):
    print('Run task %s (%s)...' % (name, os.getpid()))
    start = time.time()
    time.sleep(random.random() * 3)
    end = time.time()
    print('Task %s runs %0.2f seconds.' % (name, (end - start)))

def test(url):
    spider.Spider().run(url)

if __name__ == '__main__':
    # print('Parent process %s.' % os.getpid())
    # p = Pool(2)
    # for i in range(5):
    #     p.apply_async(long_time_task, args=(i,))
    # print('Waiting for all subprocesses done...')
    # p.close()
    # p.join()
    # print('All subprocesses done.')
    p = Pool(2)
    for i in [2, 3]:
        p.apply_async(test, args=("http://t66y.com/thread0806.php?fid=15&search=&page=%s" % i,))
    p.close()
    p.join()
