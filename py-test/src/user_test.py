import json

import urllib3

request = urllib3.PoolManager().connection_from_host(host="127.0.0.1", port=8080)

user_info = {
    "userId": "test2",
    "username": "博客园",
    "password": "test1",
    "passwordKeyId": "1",
    "phone": "123123",
    "eMail": "ggg"
}

response = request.request("post", "/user", body=json.dumps(user_info), headers={'Content-Type': 'application/json'})

if response.status != 200:
    print(response.status)
else:
    print(response.data.decode())
