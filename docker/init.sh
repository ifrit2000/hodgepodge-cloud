#!/usr/bin/env bash

docker pull redis:5.0.2
docker pull consul:1.4.0
docker pull mysql:8.0.13

docker rm $(docker stop $(docker ps -qa))

network_name=dev-network
ipAddress=172.18.0.0
containerVolumeDir=D:/Dev/containerVolume/dev

#创建网络
docker network create --subnet=${ipAddress}/16 ${network_name}

route -p add ${ipAddress} MASK 255.255.255.0 10.0.75.2

#创建容器
docker create --name dev-redis --network ${network_name} --ip 172.18.0.2 --volume ${containerVolumeDir}/redis:/data redis:5.0.2 redis-server /data/redis.conf
docker create --name dev-consul --network ${network_name} --ip 172.18.0.3 --volume ${containerVolumeDir}/consul:/data consul:1.4.0 consul agent -server -bootstrap-expect=1 -data-dir /data -client 0.0.0.0 -bind 172.18.0.3 -ui
docker create --name dev-mysql -e MYSQL_ROOT_PASSWORD=123456 --network ${network_name} --ip 172.18.0.4 --volume ${containerVolumeDir}/mysql:/var/lib/mysql mysql:8.0.13

