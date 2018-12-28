import base64
import json

import urllib3

http_pool = urllib3.PoolManager()

fields = {"keyId": "4b550e92b5054f08a58525f81403d196",
          "data": "Vh5MVlxPgZpc0VSY/qghCQx6rIYRJSIdXr9dJpuDac2JTachyOWMp4KaeNBLCSiJhQDeeKgi9EzmH2BL8f05HKGMxc+Ob2ynckXrNymRVOZBv+pd26t/V9NVJy+IIirHgE5rNe0e+aAR6FhO4zuy65F3+TdQ5R9kUBKpEWbKKzIDRwd5r6a31Jx+zuxp6hPXPg+uwbkKurxpKpALoEdxm6n5I6LxY9KFs+cHKW4jXlFkC2OjBYCwU1P6fV69pp6K4l5cTfGYeEmA/Po1Qpu+ABXDJRFHODUC1xZZpsNUbeLd1PwWNdEB4iZ8m6CRXo7zCqd5ErFAtaXFOXpXjL8msg=="}

header = {'Content-Type': 'application/json'}

response = http_pool.request('POST', 'http://127.0.0.1:8080/rsa/decode', body=json.dumps(fields), headers=header)

result = response.data.decode()

data = json.loads(result)
b64data = data['data']
print(base64.b64decode(b64data).decode())
