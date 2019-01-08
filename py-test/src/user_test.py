import json
import os

import urllib3

import cipher_test


def register(user_info):
    key_id, public_key = cipher_test.get_public_key(0)
    user_info["passwordKeyId"] = key_id
    user_info["password"] = cipher_test.encode(public_key, user_info["password"])

    request = urllib3.PoolManager().connection_from_host(host="127.0.0.1", port=8080)

    response = request.request("post", "/user", body=json.dumps(user_info),
                               headers={'Content-Type': 'application/json'})

    if response.status != 200:
        print(response.status)
    else:
        print(response.data.decode())


def login(user_id, password):
    key_id, public_key = cipher_test.get_public_key()
    password = cipher_test.encode(public_key, password)
    request = urllib3.PoolManager().connection_from_host(host="127.0.0.1", port=8080)
    response = request.request("POST", os.path.join("/auth/", user_id, key_id), body=password,
                               headers={'Content-Type': 'application/json'})
    if response.status != 200:
        print(response.status)
    else:
        print(response.data.decode())


# login("test03",
#       "akrwZt/hhxmComK9yMQWxzDsgD3PmAcM32Y/6IFSXW4krY/kdJKTKzWNNpXQ4nowcbsWytO5GLu9o0nd0ivY9U/mYVPxlgQxIhYrsXjIt0lHtZWOiRflXXNZ0/o9krc7Hhz91of1R2UfRvqvjRbxDNWfih+/HRYpI0IMil85p3DAW74RLvLDW31MOxDSkH4pKHvyxthC7MGU1ammmZedhwbYvA1X8Kw50AE1t9UK/OnIEAVNwuEWwoIUPUJiA2WdGccCYwjkr3kQxbTghL7cNieitEWA1ddFz9kiGYN64uvDrYIg2+9CIv8JchQid14af5MY1E2jA8/4lbHzjyjA8g==")

# register()
if __name__ == '__main__':
    user_info = {
        "userId": "cdistc",
        "username": "博客园",
        "password": "1234561",
        "phone": "123123",
        "eMail": "ggg"
    }
    # register(user_info)
    login(user_info["userId"], user_info["password"])
