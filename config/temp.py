# -* coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""
import json
import urllib3


def deregister(consul_host='172.28.0.3', consul_port='8500',node="3adb1371e245"):
    service_info_url = '%s:%s/v1/health/node/%s' % (consul_host, consul_port,node)
    http = urllib3.PoolManager()
    r = http.request('get', service_info_url)
    if 200 == r.status:
        res = r.data.decode('utf-8')
        services = json.loads(res)
        for service in services:
            if service['Status']=='critical':
                print(service['ServiceID'])
        # for k, v in services.items():
                http.request('put', 'http://172.28.0.3:8500/v1/agent/service/deregister/' + service['ServiceID'])
        #     print("deregister service:", k)


deregister()
