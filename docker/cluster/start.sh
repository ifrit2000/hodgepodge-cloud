#!/usr/bin/env bash

docker-compose -f cluster.yml -f mysql.yml -f redis.yml -f zookeeper.yml up