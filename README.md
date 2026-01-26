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

1. `TypeScript`/`JavaScript`接入

    ```typescript
    
    ```

    ```javascript
    
    ```

2. `Golang`接入

    ```golang
    
    ```

3. `Python`接入

    ```python

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
