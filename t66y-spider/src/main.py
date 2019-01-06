from spider import Daemon, task

if __name__ == '__main__':
    daemon = Daemon(task=task, task_args=["2", "15"])
    daemon.start()
