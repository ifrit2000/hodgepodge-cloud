#!/usr/bin/env bash

conda create -n t66y python=3.7 -y

conda activate t66y

conda install urllib3 beautifulsoup4 pymysql redis-py html5lib -y

pip install pyqt5