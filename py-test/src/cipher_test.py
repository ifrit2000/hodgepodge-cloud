import base64
import json

import urllib3
from Crypto.Cipher import PKCS1_v1_5
from Crypto.PublicKey import RSA

CHAR_SET = 'utf-8'


def build_http_request():
    return urllib3.PoolManager().connection_from_host(host="127.0.0.1", port=8080)


def get_public_key():
    response = build_http_request().request("get", '/rsa/publicKey')
    result = json.loads(response.data.decode())['data']
    return result['keyId'], result['publicKey']


def encode(public_key, data):
    rsa_public_key = RSA.importKey("-----BEGIN PUBLIC KEY-----\n%s\n-----END PUBLIC KEY-----" % public_key)
    rsa_encoder = PKCS1_v1_5.new(rsa_public_key)
    return base64.b64encode(rsa_encoder.encrypt(data.encode(CHAR_SET))).decode(CHAR_SET)


def decode():
    pass


if __name__ == "__main__":
    key_id, public_key = get_public_key()
    test = encode(public_key, '123')
    print(test)
