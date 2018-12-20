#!/usr/bin/env bash

network_name=dev-network
ipAddress=172.18.0.0

#创建网络
docker network create --subnet=${ipAddress}/16 ${network_name}
#添加路由
route -p add ${ipAddress} MASK 255.255.255.0 10.0.75.2

route add -net 11.0.0.0/8 gw 10.31.167.247 eth0

sudo route add -net 172.18.0.0 netmask 255.255.255.0 gw 172.18.0.1

