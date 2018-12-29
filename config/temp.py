# -* coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""
import json
import urllib3


def deregister(consul_host='172.28.0.3', consul_port='8500'):
    service_info_url = '%s:%s/v1/agent/services' % (consul_host, consul_port)
    http = urllib3.PoolManager()
    r = http.request('get', service_info_url)
    if 200 == r.status:
        res = r.data.decode('utf-8')
        services = json.loads(res)
        for k, v in services.items():
            print(k)
            print(v)
            http.request('put', 'http://172.28.0.3:8500/v1/agent/service/deregister/' + k)


deregister()
