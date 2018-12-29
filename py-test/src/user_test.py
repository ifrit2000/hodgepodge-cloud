import json

import urllib3

import cipher_test


def register():
    key_id, public_key = cipher_test.get_public_key(0)

    password = cipher_test.encode(public_key, "123123123")

    request = urllib3.PoolManager().connection_from_host(host="127.0.0.1", port=8080)

    user_info = {
        "userId": "aaaa1",
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
    request.request("POST","/auth/"+user_id,body=json.dumps(password),
                    headers={'Content-Type': 'application/json'})


login("aaaa1",
      "KkEj2Ddcpm4zrr/y8GQsOc4TI2+HJ+G8w0cyW4Wdjli5sGgk3Ta6xHZm+ER3TiTn11jDOCIX+FwsUx6vlM/IbqWGGIczV9Jc+LGg50iCNaxLYSSFBT5uWkCG118vdbjne5Q2lbZUcLLIL0KjT/53m4HhMa84l+XmyXQdhNLsLku2WkoBYMpR18CvEkJmFIY/hpshfnvctH3Nnhak4F0mw2tZLRTWpPuR8AiS0eVtjfy5zrzutd6/rO3Ov4Mj4CcZM8A9QtMXVkA84kGaqdJiicR2VfixxEHai0w/NUBe0/ln25JlaZ1wEoBJE09FIYtVhKGkKpLoaOKPjs6mqu2ECg==")

# register()
