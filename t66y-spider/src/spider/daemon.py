import atexit
import errno
import multiprocessing
import os
import signal
import sys
import time

from spider.logger import get_logger


def test():
    f = open("12", "a")
    f.write("123\n")
    f.close()
    return


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
            Daemon.logger().error(e, file=sys.stderr)
            raise SystemExit(1)
        self.run()

    def run(self):
        Daemon.logger().info("start")
        while True:
            try:
                p = multiprocessing.Pool(1)
                p.apply(test)
                p.close()
            except Exception as e:
                Daemon.logger().error(e, file=sys.stderr)
            time.sleep(1)

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
