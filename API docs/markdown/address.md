# Property Booking Website

# address

## POST 增加地址

POST /api/v1/addresses

> Body Parameters

```json
{
  "districtId": 8,
  "street": "大仁街40號"
}
```

### Params

|Name|Location|Type|Required|Title|Description|
|---|---|---|---|---|---|
|Authorization|header|string| yes ||none|
|» districtId|body|integer| yes | 地區 ID|none|
|» street|body|string| yes | 街道名稱|none|

> Response Examples

> addresses

### Responses

|HTTP Status Code |Meaning|Description|Data schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|addresses|Inline|

### Responses Data Schema

## GET 取得指定地址資訊

GET /api/v1/addresses/{addressId}

### Params

|Name|Location|Type|Required|Title|Description|
|---|---|---|---|---|---|
|addressId|path|string| yes ||地址 ID|

> Response Examples

> 200

```json
{
  "addressId": 1,
  "street": "test street 1",
  "adminAreaLevel3DistrictId": 181,
  "districtName": "宜蘭市"
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
|» addressId|integer|true|none|地址 ID|none|
|» street|string|true|none|街道名稱|none|
|» adminAreaLevel3DistrictId|integer|true|none|第三級行政區 ID|none|
|» districtName|string|true|none|行政區名稱|none|


