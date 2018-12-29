import json

import urllib3

import cipher_test


def register():
    key_id, public_key = cipher_test.get_public_key(0)

    password = cipher_test.encode(public_key, "123123123")

    request = urllib3.PoolManager().connection_from_host(host="127.0.0.1", port=8080)

    user_info = {
        "userId": "test03",
        "username": "博客园",
        "password": password,
        "passwordKeyId": key_id,
        "phone": "123123",
        "eMail": "ggg"
    }
    print(password)
    response = request.request("post", "/user", body=json.dumps(user_info),
                               headers={'Content-Type': 'application/json'})

    if response.status != 200:
        print(response.status)
    else:
        print(response.data.decode())


def login(user_id, password):
    request = urllib3.PoolManager().connection_from_host(host="127.0.0.1", port=8080)
    request.request("POST","/auth/"+user_id,body=password,
                    headers={'Content-Type': 'application/json'})


login("test03",
     "akrwZt/hhxmComK9yMQWxzDsgD3PmAcM32Y/6IFSXW4krY/kdJKTKzWNNpXQ4nowcbsWytO5GLu9o0nd0ivY9U/mYVPxlgQxIhYrsXjIt0lHtZWOiRflXXNZ0/o9krc7Hhz91of1R2UfRvqvjRbxDNWfih+/HRYpI0IMil85p3DAW74RLvLDW31MOxDSkH4pKHvyxthC7MGU1ammmZedhwbYvA1X8Kw50AE1t9UK/OnIEAVNwuEWwoIUPUJiA2WdGccCYwjkr3kQxbTghL7cNieitEWA1ddFz9kiGYN64uvDrYIg2+9CIv8JchQid14af5MY1E2jA8/4lbHzjyjA8g==")

# register()
