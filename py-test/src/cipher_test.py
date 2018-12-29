import base64
import json

import urllib3
from Crypto.Cipher import PKCS1_v1_5
from Crypto.PublicKey import RSA

CHAR_SET = 'utf-8'


def build_http_request():
    return urllib3.PoolManager().connection_from_host(host="127.0.0.1", port=8082)


def get_public_key():
    response = build_http_request().request("get", '/rsa/publicKey')
    result = json.loads(response.data.decode())['data']
    return result['keyId'], result['publicKey']


def encode(public_key, data):
    rsa_public_key = RSA.importKey("-----BEGIN PUBLIC KEY-----\n%s\n-----END PUBLIC KEY-----" % public_key)
    rsa_encoder = PKCS1_v1_5.new(rsa_public_key)
    return base64.b64encode(rsa_encoder.encrypt(data.encode(CHAR_SET))).decode(CHAR_SET)


def decode(key_id, data):
    body = {"keyId": key_id, "data": data}
    header = {'Content-Type': 'application/json'}
    response = build_http_request().request("post", '/rsa/decode', headers=header, body=json.dumps(body))
    result = json.loads(response.data.decode())['data']
    return base64.b64decode(result).decode()


if __name__ == "__main__":
    key_id, public_key = get_public_key()
    data = 'JS排序:localeCompare() 方法实现中文排序、sort方法实现..._博客园'
    encode_data = encode(public_key, data)
    print(encode_data)
    decode_data = decode(key_id, encode_data)
    print(decode_data)
    print(decode_data == data)
