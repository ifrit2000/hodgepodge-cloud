#!/usr/bin/env bash

docker pull redis:5.0.2
docker pull consul:1.4.0
docker pull mysql:8.0.13

docker stop $(docker ps -qa)
docker rm $(docker ps -qa)

network_name=dev-network
ipAddress=172.18.0.0

#创建网络
docker network create --subnet=${ipAddress}/16 ${network_name}

route -p add ${ipAddress} MASK 255.255.255.0 10.0.75.2

#创建容器
docker create --name dev-redis --network ${network_name} --ip 172.18.0.2 redis:5.0.2
docker create --name dev-consul --network ${network_name} --ip 172.18.0.3 consul:1.4.0
