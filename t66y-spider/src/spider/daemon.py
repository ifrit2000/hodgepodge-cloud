import atexit
import errno
import multiprocessing
import os
import signal
import sys
import time



def test():
    f = open("12", "a")
    f.write("123")
    f.close()
    return


class Daemon:
    def __init__(self, pid_file='test.pid', stdin='/dev/null', stdout='/dev/null', stderr='/dev/null'):
        self.__pid_file = pid_file
        self.__stdin = stdin
        self.__stdout = stdout
        self.__stderr = stderr


    def daemonize(self):
        try:
            # 第一次fork，生成子进程，脱离父进程
            if os.fork() > 0:
                raise SystemExit(0)  # 退出主进程
        except OSError as e:
            logyyx.error("fork #1 failed:\n")
            # sys.exit(1)
            raise RuntimeError('fork #1 faild: {0} ({1})\n'.format(e.errno, e.strerror))
        os.chdir("/")  # 修改工作目录
        os.setsid()  # 设置新的会话连接
        os.umask(0)  # 重新设置文件创建权限
        try:
            # 第二次fork，禁止进程打开终端
            if os.fork() > 0:
                raise SystemExit(0)
        except OSError as e:
            logyyx.error("fork #2 failed:\n")
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
    def __wait_child(signum, frame):
        logyyx.info('receive SIGCHLD')
        try:
            while True:
                # -1 表示任意子进程
                # os.WNOHANG 表示如果没有可用的需要 wait 退出状态的子进程，立即返回不阻塞
                cpid, status = os.waitpid(-1, os.WNOHANG)
                if cpid == 0:
                    logyyx.info('no child process was immediately available')
                    break
                exitcode = status >> 8
                logyyx.info('child process %s exit with exitcode %s', cpid, exitcode)
        except OSError as e:
            if e.errno == errno.ECHILD:
                logyyx.error('current process has no existing unwaited-for child processes.')
            else:
                raise
        logyyx.info('handle SIGCHLD end')

    @staticmethod
    def __sigterm_handler(signo, frame):
        raise SystemExit(1)

    def start(self):
        if os.path.exists(self.__pid_file):
            print("the deamon is already running!!!")
            return
        try:
            self.daemonize()
        except RuntimeError as e:
            print(e, file=sys.stderr)
            raise SystemExit(1)
        self.run()

    def run(self):
        while True:
            try:
                p = multiprocessing.Pool(1)
                p.apply(test)
                p.close()

            except:
                pass
            time.sleep(10)

    def stop(self):
        try:
            if os.path.exists(self.pidFile):
                with open(self.pidFile) as f:
                    os.kill(int(f.read()), signal.SIGTERM)
            else:
                print('Not running.', file=sys.stderr)
                raise SystemExit(1)
        except OSError as e:
            if 'No such process' in str(e) and os.path.exists(self.pidFile):
                os.remove(self.pidFile)

    def restart(self):
        self.stop()
        self.start()


if __name__ == "__main__":
    LOG = './tsl.log'
    daemon = Daemon(stdout=LOG, stderr=LOG)

    # daemon.start()
    if len(sys.argv) != 2:
        print('Usage: {} [start|stop]'.format(sys.argv[0]))
        raise SystemExit(1)
    if 'start' == sys.argv[1]:
        daemon.start()
    elif 'stop' == sys.argv[1]:
        daemon.stop()
    elif 'restart' == sys.argv[1]:
        daemon.restart()
    else:
        print('Unknown command {0}'.format(sys.argv[1]))
        raise SystemExit(1)
