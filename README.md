# WikiBroker OpenAPI

WikiBroker是一个SaaS服务，它提供了大量OpenAPI以便于定制开发客户端，开发者可以通过这里提供的SDK实现接入。

## 接入说明

WikiBroker的OpenAPI接口都会校验请求

1. 应用认证：请求必须包含合法有效的应用认证标识ApiKey。
2. 请求限流：每个ApiKey的请求频率不能超过50/秒。
3. 防延时：请求时间戳与服务端时间戳的偏差必须小于一定阈值。
4. 防重放：相同唯一标识的重复请求会被拦截。
5. 防篡改：参数与签名不匹配的请求会被拦截。

## 接入方式

**在进行客户端接入前，你需要先成为WikiBroker的客户，然后联系我们为你创建专属的API Key，然后API Key会发送到你开通服务使用的邮箱中。**

### 通过SDK接入（推荐）

根据你使用的编程语言，按需选用本仓库提供的SDK。

#### `TypeScript`/`JavaScript`接入

**安装**

<details>
<summary>npm</summary>

    npm install ./wikibroker-openapi-js-sdk-1.0.0.tgz
</details>
<details>
<summary>yarn</summary>

    yarn add ./wikibroker-openapi-js-sdk-1.0.0.tgz
</details>
<details>
<summary>pnpm</summary>

    pnpm add ./wikibroker-openapi-js-sdk-1.0.0.tgz
</details>
<br/>

**示例**

`fetch`

```javascript
import { wrappedFetch } from "wikibroker-openapi-sdk";

const apiKey = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b";
const apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
const fetch = wrappedFetch(apiKey, apiSecret);

const req = new Request("https://api.example.com/test?q1=c&q2=b&q1=a", {
    method: "POST",
    body: JSON.stringify({
        key: "value",
    }),
});
fetch(req);
```

`axios`

```javascript
import { axiosHook } from "wikibroker-openapi-sdk";

const apiKey = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b";
const apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
axios.interceptors.request.use(axiosHook(apiKey, apiSecret));

axios.post(
    "https://api.example.com",
    {
        key: "value",
    },
    {
        params: {
            q1: ["c", "a"],
            q2: ["b"],
        },
    },
);
```

#### `Golang`接入

**安装**

```bash
tar zxf wikibroker-openapi-go-sdk-1.0.0.tgz
go mod edit -replace=wikibroker_openapi_sdk=./wikibroker_openapi_sdk
go get wikibroker_openapi_sdk
```

**示例**

`net/http`

```golang
import sdk "wikibroker_openapi_sdk"

const apiKey = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b"
const apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
rawClient := &http.Client{}
client := sdk.NewHttpClient(rawClient, apiKey, apiSecret)

client.Post(
    "https://api.example.com/test?q1=c&q2=b&q1=a",
    map[string]any{
        "key": "value",
    },
)
```

`resty`

```golang
import sdk "wikibroker_openapi_sdk"

const apiKey = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b"
const apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
client := resty.New()
m := sdk.NewRestyRequestMiddleware(apiKey, apiSecret)
client.AddRequestMiddleware(m)

client.R().SetBody(
    map[string]any{
        "key": "value",
    },
).SetQueryParamsFromValues(
    url.Values{
        "q1": []string{"c", "a"},
        "q2": []string{"b"},
    },
).Post("https://api.example.com/test")
```

`grequests`

```golang
import sdk "wikibroker_openapi_sdk"

const apiKey = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b"
const apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"

data, _ := json.Marshal(
    map[string]any{
        "key": "value",
    },
)
body := grequests.RequestBody(bytes.NewReader(data))
auth := sdk.GRequestsAuthOption(apiKey, apiSecret)
grequests.Post(
    context.TODO(),
    "https://api.example.com/test?q1=c&q2=b&q1=a",
    body,
    auth,
)
```

`gorequest`

```golang
import sdk "wikibroker_openapi_sdk"

const apiKey = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b"
const apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
agent := gorequest.New()
sdk.LoadGorequestInterceptor(agent, apiKey, apiSecret)

data, _ := json.Marshal(
    map[string]any{
        "key": "value",
    },
)
body := string(data)
agent.Post("https://api.example.com/test?q1=c&q2=b&q1=a").Send(body).End()
```

#### `Python`接入

**安装**

<details>
<summary>pip</summary>

    pip install ./wikibroker_openapi_sdk-1.0.0a0-py3-none-any.whl
</details>
<details>
<summary>poetry</summary>

    poetry add ./wikibroker_openapi_sdk-1.0.0a0-py3-none-any.whl
</details>
<details>
<summary>uv</summary>

    uv add ./wikibroker_openapi_sdk-1.0.0a0-py3-none-any.whl
</details>
<br/>

**示例**

`requests`

```python
import requests
from wikibroker_openapi_sdk import build_requests_auth

API_KEY = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b"
API_SECRET = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
auth = build_requests_auth(api_key=API_KEY, api_secret=API_SECRET)

requests.post(
    url="https://api.example.com/test",
    params={"q1": ["c", "a"], "q2": ["b"]},
    json={"key": "value"},
    auth=auth,
)
```

`httpx`

```python
import httpx
from wikibroker_openapi_sdk import build_httpx_auth

API_KEY = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b"
API_SECRET = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
auth = build_httpx_auth(api_key=API_KEY, api_secret=API_SECRET)

httpx.post(
    url="https://api.example.com/test",
    params={"q1": ["c", "a"], "q2": ["b"]},
    json={"key": "value"},
    auth=auth,
)
```

`aiohttp`

```python
import aiohttp
from wikibroker_openapi_sdk import build_aiohttp_auth

API_KEY = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b"
API_SECRET = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
auth = build_aiohttp_auth(api_key=API_KEY, api_secret=API_SECRET)

async def send_request():
    async with aiohttp.ClientSession() as session:
        await session.post(
            url="https://api.example.com/test",
            params={"q1": ["c", "a"], "q2": ["b"]},
            data={"key": "value"},
            auth=auth,
        )

send_request()
```

### 通过API接入

如果你使用的编程语言没有可用的SDK，可以按照以下方式自行编写接入代码。

1. 添加自定义请求头
   1. `X-Api-Key`：应用访问令牌，是一个`uuid`格式的字符串。
   2. `X-Timestamp`：请求绝对时间戳毫秒数，是一个整数。
   3. `X-Nonce`：请求唯一标识，是一个`uuid`格式的随机字符串。
   4. `X-Signature`：请求数字签名，根据指定算法生成。

2. 签名生成算法
   1. 将请求查询参数按`key`的字典序升序排列，对于相同`key`则再按`value`的字典序升序排列，然后按`{key}={value}`的格式用`&`拼接，构成规范化查询字符串`canonical_query`。
   2. 计算请求体的`sha256`哈希并转换为16进制编码字符串`body_hash`。
   3. 用换行符拼接大写请求方法、请求相对路径、`canonical_query`、`X-Api-Key`、`X-Timestamp`、`X-Nonce`、`body_hash`，然后生成`hmac-sha256`哈希并转换为16进制编码字符串，得到请求签名`X-Signature`。
