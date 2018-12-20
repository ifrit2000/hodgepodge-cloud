#!/usr/bin/env bash

network_name=dev-network
ipAddress=172.18.0.0
containerVolumeDir=D:/Dev/containerVolume/dev
docker rm dev-consul
#docker create --name dev-redis --network ${network_name} --ip 172.18.0.2 --volume ${containerVolumeDir}/redis:/data redis:5.0.2 #redis-server /data/redis.conf
docker create --name dev-consul --network ${network_name} --ip 172.18.0.3 --volume ${containerVolumeDir}/consul:/consul/data consul:1.4.0 consul agent -server -bootstrap-expect 1 -data-dir /consul/data -client 0.0.0.0 -bind 172.18.0.3 -ui
#docker create --name dev-mysql -e MYSQL_ROOT_PASSWORD=123456 --network ${network_name} --ip 172.18.0.4 --volume ${containerVolumeDir}/mysql:/var/lib/mysql mysql:8.0.13

docker start dev-consul

docker logs -f -t --tail 10 dev-consul