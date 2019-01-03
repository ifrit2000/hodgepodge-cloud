import sys

from spider.daemon import Daemon

if __name__ == "__main__":

    daemon = Daemon()
    daemon.start()
    # if len(sys.argv) != 2:
    #     print('Usage: {} [start|stop]'.format(sys.argv[0]))
    #     raise SystemExit(1)
    # if 'start' == sys.argv[1]:
    #     daemon.start()
    # elif 'stop' == sys.argv[1]:
    #     daemon.stop()
    # elif 'restart' == sys.argv[1]:
    #     daemon.restart()
    # else:
    #     print('Unknown command {0}'.format(sys.argv[1]))
    #     raise SystemExit(1)
