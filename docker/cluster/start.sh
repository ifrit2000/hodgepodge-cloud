#!/usr/bin/env bash

docker-compose -f cluster.yml -f mysql.yml -f redis.yml -f consul.yml up
#for i in "$@"; do
#    echo ${i}
#done


