# Property Booking Website

# auth

## POST 使用者註冊

POST /api/v1/auth/register

> Body Parameters

```json
{
  "ecUserUsername": "test12",
  "ecUserPassword": "password12",
  "ecUserName": "test12",
  "ecUserEmail": "test12"
}
```

### Params

|Name|Location|Type|Required|Title|Description|
|---|---|---|---|---|---|
|body|body|object| no ||none|
|» ecUserUsername|body|string| yes | 帳號|none|
|» ecUserPassword|body|string| yes | 密碼|none|
|» ecUserName|body|string| yes | 姓名|none|
|» ecUserEmail|body|string| yes | 電子信箱地址|none|

> Response Examples

> 200

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0MTIiLCJpYXQiOjE3MTM4NjQyNTgsImV4cCI6MTcxMzk1MDY1OH0.SDV6sqPQDSTqUH0L-xIT-UmcUyQEmF-Uspl2Pi0ZRWkWco3WpQT9LP4OaH7Yn3Dm"
}
```

### Responses

|HTTP Status Code |Meaning|Description|Data schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|200|Inline|

### Responses Data Schema

HTTP Status Code **200**

|Name|Type|Required|Restrictions|Title|description|
|---|---|---|---|---|---|
|» token|string|true|none|令牌|驗證使用者身份的令牌|

## POST 使用者登入

POST /api/v1/auth/login

> Body Parameters

```json
{
  "ecUserUsername": "user1",
  "ecUserPassword": "123456"
}
```

### Params

|Name|Location|Type|Required|Title|Description|
|---|---|---|---|---|---|
|body|body|object| no ||none|
|» ecUserUsername|body|string| yes | 帳號|none|
|» ecUserPassword|body|string| yes | 密碼|none|

> Response Examples

> 200

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcxMzg2NTEyMCwiZXhwIjoxNzEzOTUxNTIwfQ.QO23Sm9s8FrxpNbt6rlI2OVDBsNHNFSjvxWEwz-P01iiXslsZMoVLoesS7PLvxNj"
}
```

### Responses

|HTTP Status Code |Meaning|Description|Data schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|200|Inline|

### Responses Data Schema

HTTP Status Code **200**

|Name|Type|Required|Restrictions|Title|description|
|---|---|---|---|---|---|
|» token|string|true|none|令牌|驗證使用者身份的令牌|


