# import json
#
# import urllib3
#
# http = urllib3.PoolManager()
#
# response = http.request('get', 'http://localhost:8080/rsa/publicKey?expire=0')
#
# result = response.data.decode('utf-8')
# # print(result)
# # json.loads(result)
#
#
# publicKey = json.loads(result)['data']['publicKey']
# # publicKey=json.loads(result).data.publicKey
# key = base64.b64decode(publicKey)
import base64

from Crypto.Cipher import PKCS1_v1_5
from Crypto.PublicKey import RSA

CHAR_SET = 'utf-8'
publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlibBPn5iPZDAx0xUhlSAGiq58NZ3yirEHx7mc2kar/Lz9pRx7qgawW27PqCpfmhd5M/WK/e9Y6ix0rcF0w6T81vsSDTJJsJZHp6LxVC6Fb1Blna1PuDNExroVXcxBa0/yp0gMMVzYI/egDGsybSTH+xCs+Eh9T8xZ/H9pWSvS5lYpjaHHj1NPpV4/t8DsOKC2vCYrZbeWtwJAQgbuNRcxaX6O0FHs6Xwm470qmrgC8ozw8pRHnPMTO8feJ2iQOsP2sb66syOlfZjdxyDruISfBQqvPgg+HQkWBZtUCfangv/vyuglvaynOxcd/VGxbB6Zra6iiENTRYB+jO9YDhpnwIDAQAB"
privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCWJsE+fmI9kMDHTFSGVIAaKrnw1nfKKsQfHuZzaRqv8vP2lHHuqBrBbbs+oKl+aF3kz9Yr971jqLHStwXTDpPzW+xINMkmwlkenovFULoVvUGWdrU+4M0TGuhVdzEFrT/KnSAwxXNgj96AMazJtJMf7EKz4SH1PzFn8f2lZK9LmVimNocePU0+lXj+3wOw4oLa8Jitlt5a3AkBCBu41FzFpfo7QUezpfCbjvSqauALyjPDylEec8xM7x94naJA6w/axvrqzI6V9mN3HIOu4hJ8FCq8+CD4dCRYFm1QJ9qeC/+/K6CW9rKc7Fx39UbFsHpmtrqKIQ1NFgH6M71gOGmfAgMBAAECggEAMJNlHSSh/6ze0RselT6tGsoL0aBwrJTdUfwbLeco3RdKWdF4cm8sCLvJQd+UNfLpvWaHsT26pY0jyjmvxrIGp0przIhXMxTY5BECwtj0+qd5moXY6PitH6sq0st2rpF3+8KNcXnPc8PXhb2MWszyc/dpNOx2ofLJtwkQt/s0ws3fjl2ZtlDbBTIM/E27ZCvw8ENvGkKeuHcERLkZB9rMPs6pnM8ICf1eDyCBquEVtBgDYb65MlFWg5TYHfwSbIxNfkwh8yEaz9W7kBjDvPgiwKXZqsoMLmhsB2fUOsoaWJhxdWWtICmI2mrCxYk9KQ2zwM3YoLAkXl9peD70+7zdUQKBgQDd3H+vNYZAv6Prmt5vMQFHZ4b2hg+KQAFtKl6GjzW4xxaLTVCJMIzGp+kOMgphevCTlxLSV0wunWzdSJ0OHKgbq+qAT0ZjD8sgt2xVyU+Z6pFQj/kz/tfbNIP+oHGJ8HLh/18uwLNsgtIChb7JG6sKHHJwniClXGwmr9HW66jUNQKBgQCtQXpHEPz3m04pBkXLOLjFElSbipb9OqMCeTb54ApwZLjqaHBV40P0aH8hz5fDfvvHOdo1qtiXa4OQ81B7TzBoO/p+JGLRLC86d8T/V2LmxgDIothAf/E2FnYDiAH7vSPsDZ218CqU5k8Hhe4b4faaA0x1Nsfcea+Ij7VFTYnZAwKBgDx8/aL7aNsGZN67nqGaLssVAsr7ygjbYogs4RC2wuLaBN99+NMulXMkHHpuUj45kpXqvorymiarbR73yTorvfmtaYYKFxqzF6KX38WT2UwRlATu+/adKKTvMH2fqNT+5ZOQWJcamtCe6jsd0+Jo1L0w/FKQCj6LcEEr9n4uSh1xAoGAeopSQwt38GVPLeL64Fa5EOH7J5wpWOftPaWgRbG5kG8c/uZpdcXtXWO/b5mVfLdGu01m0giJcuefQZlmdiC3WzH45Nk1bz6yFMd7dSJImHK5QS80hsI3SAsw4ySCSpwnWSD0SCea5n/Sq76FgAEdWyc0H79kMsN6bLs/+cly0yMCgYBqcjjnNWE8dXRnt83F5SykBas5YilG8zqvcXaKJAKfQH/PHflrxvUva98zMYhWXlk2H5bBLSfUoMYNSM3RM78GKFtvkMxyu0a/c0lo9UA8ZQAKv3VLommYdpcynHKbBWPiTq1Rapu7HvD0N26wsrmcKDVQZCfK+pzQRNN8EcqXoA=="

python_public_key = "-----BEGIN PUBLIC KEY-----\n%s\n-----END PUBLIC KEY-----"

public_key = RSA.importKey(python_public_key % publicKey)
rsa_encoder = PKCS1_v1_5.new(public_key)
text = 'ascii字符，肉眼不可理解'
text = str(base64.b64encode(rsa_encoder.encrypt(text.encode(CHAR_SET))), CHAR_SET)

print(text)
