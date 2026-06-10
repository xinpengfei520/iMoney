# iMoney API 接口文档

> 供 **APP 端对接** 使用。本文档由后端代码（`com.vance.xin.imoney.*`）逐接口核对生成，
> 路径 / 方法 / 参数 / 响应均来自实际实现，示例响应取自生产环境真实调用。

| 项 | 值 |
|---|---|
| 服务版本 | **v1.0.8**（2026-06-09 已上线） |
| 后端 | Spring Boot 4 / Java 21，端口 `8001` |
| 业务面前缀 | 所有接口均以 `/imoney` 开头 |
| 字符编码 | 全部 `UTF-8`（部分静态 JSON 带 UTF-8 BOM，见 §6） |
| 是否需要 `app_id` 头 | **不需要**（`/imoney/**` 不在 `AppInterceptor` 范围内，与 `/book`、`/asflow` 不同） |

---

## 0. 公网入口（已开通）

- **对接基础 URL**：`https://api.vance.xin`
  - 例：`https://api.vance.xin/imoney/token/login`
- **状态**：`/imoney/**` 的公网路由已于 **2026-06-09 开通并验证**（生产 Nginx location 已放行 `^/(book|asflow|imoney)(/|$)` → 后端 `8001`）。`/imoney/Bag`、`/imoney/json/index.json`、`/imoney/token/login` 等均实测 200，受保护接口无 token 实测 401。
- **协议**：HTTPS（`api.vance.xin` 已配置证书；HTTP 会 301 跳 HTTPS）。

> 下文示例为简洁起见使用占位 `{BASE}`，对接时替换为 `https://api.vance.xin`。

---

## 1. 鉴权说明

### 1.1 机制
- 采用 **JWT**（HS512，有效期 30 天）。
- 通过 `/imoney/token/login` 登录获取 token，token 形如 `Bearer eyJ0eXAiOi...`（**注意：值本身已含 `Bearer ` 前缀**）。
- 调用受保护接口时，把它放进请求头：
  ```
  Authorization: Bearer eyJ0eXAiOi...
  ```
  > 兼容写法：也可以用同名 **查询参数** `Authorization` 传递（后端先读 header，没有再读 query param）。推荐用 header。

### 1.2 哪些接口需要 token？
只有下表标 **🔒 需要 token** 的接口需要鉴权；其余接口（静态数据、空气质量、登录、七牛、投资等）**开放访问**。

| 接口 | 鉴权 |
|---|---|
| `GET /imoney/user/{id}` | 🔒 需要 token |
| `GET /imoney/customer/{id}` | 🔒 需要 token |
| `GET /imoney/student/all` | 🔒 需要 token |
| `GET /imoney/student/{id}` | 🔒 需要 token |
| `POST /imoney/customer/add` | 🔓 开放（注册） |
| 其余全部接口 | 🔓 开放 |

### 1.3 token 失效响应
未带 / 非法 / 过期 token 访问受保护接口，返回 **HTTP 401**，信封格式：
```json
{ "data": null, "meta": { "success": false, "message": "Token is invalid" } }
```

---

## 2. 通用响应格式

iMoney 存在 **三种** 响应体形态，APP 端解析时需区分：

1. **信封格式 `{meta, data}`** —— 用于登录登出、以及**所有异常响应**（含上面的 401）：
   ```json
   { "meta": { "success": true, "message": "ok" }, "data": { ... } }
   ```
   - `meta.success`：布尔，是否成功。
   - `meta.message`：`"ok"` 或错误描述。
   - `data`：成功时的业务数据，可能为对象 / 字符串 / null。

2. **裸实体 JSON** —— 数据查询接口**成功时**直接返回实体或实体数组（**不带** `meta` 信封）。
   > ⚠️ 重要：这些接口**成功**是裸 JSON，**失败**（如 token 失效、500）却是 `{meta,data}` 信封。APP 端要按 HTTP 状态码先判成败，再决定用哪种结构解析。

3. **原样字节流** —— 静态 JSON / 空气质量 / 投资 HTML 等，直接返回文件内容（`Content-Type: application/json;charset=UTF-8` 或 `text/html;charset=UTF-8`）。

### 2.1 异常 → HTTP 状态码对照（异常时统一返回 `{meta,data}` 信封）

| 场景 | HTTP | `meta.message` |
|---|---|---|
| token 缺失/非法/过期 | 401 | `Token is invalid` |
| 请求体 JSON 无法解析 | 400 | `could_not_read_json` |
| 参数校验失败（如 `name` 为空） | 400 | `parameter_validation_exception` |
| HTTP 方法不支持 | 405 | `request_method_not_supported` |
| Content-Type 不支持 | 415 | `content_type_not_supported` |
| 其它服务端异常 | 500 | `Internal Server Error` |

---

## 3. 接口明细

> 说明：部分接口在后端用的是不限方法的 `@RequestMapping`（历史 servlet 行为，技术上 GET/POST 都能命中）。下表给出**推荐方法**；标「任意方法」的接口用 GET 即可。

### 3.1 认证

#### ① 登录（签发 JWT）
- **`POST {BASE}/imoney/token/login`**
- 鉴权：🔓 开放
- 参数（**Query 或 form**，`@RequestParam`，必填）：

  | 参数 | 类型 | 必填 | 说明 |
  |---|---|---|---|
  | `name` | string | 是 | 用户名（样例数据：`Vance` / `Jack` / `Lin` / `Lee`） |
  | `password` | string | 是 | 密码（样例数据均为 `123`） |

- **成功响应**（HTTP 200）：响应头额外带 `Authorization: Bearer xxx`，body：
  ```json
  {
    "data": {
      "token": "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0Ijo...",
      "userId": 1
    },
    "meta": { "success": true, "message": "ok" }
  }
  ```
  > APP 端把 `data.token` 整串（含 `Bearer `）原样存下，后续请求放进 `Authorization` 头即可。

- **失败响应**（账号密码错误，仍是 HTTP 200）：
  ```json
  { "data": null, "meta": { "success": false, "message": "Login Failure..." } }
  ```
- 示例：
  ```bash
  curl -X POST "{BASE}/imoney/token/login?name=Vance&password=123"
  ```

#### ② 登出
- **`POST {BASE}/imoney/token/logout`**
- 鉴权：🔓 开放
- 参数：无
- 响应（HTTP 200）：
  ```json
  { "data": "Logout Success...", "meta": { "success": true, "message": "ok" } }
  ```
  > JWT 无状态，登出仅返回成功语义；客户端自行丢弃本地 token 即可。

#### ③ App 登录（旧 LoginServlet，硬编码校验）
- **`POST {BASE}/imoney/login`**（任意方法）
- 鉴权：🔓 开放
- 与 `①` 是**两个不同接口**，此接口为旧客户端兼容保留。
- 参数（Query 或 form，均选填）：

  | 参数 | 类型 | 说明 |
  |---|---|---|
  | `username` | string | 固定账号 `13212341234` |
  | `password` | string | 固定密码 = `MD5("123456")` = `e10adc3949ba59abbe56e057f20f883e`（即客户端需传 123456 的 MD5） |

- **成功**（账号密码命中）：返回 `json/login.json` 文件内容（JSON 字节流，HTTP 200）。
- **失败**：HTTP 200，body：
  ```json
  {"success":false}
  ```

---

### 3.2 用户 / 客户

> 数据模型见 §5.1 `ImoneyUser`。⚠️ 当前实现的查询响应**包含 `password` 字段**（沿用旧行为），APP 端按需忽略。

#### ④ 按 id 查用户
- **`GET {BASE}/imoney/user/{id}`**（任意方法）
- 鉴权：🔒 需要 token
- 路径参数：`id`（int）
- 成功响应（HTTP 200，裸实体）：
  ```json
  {"city":"Shanghai","email":"Vance@qq.com","gentle":"男","id":1,"name":"Vance","password":"123"}
  ```
  > 查无此人返回 HTTP 200 + 空 body。
- 示例：
  ```bash
  curl -H "Authorization: Bearer eyJ0..." "{BASE}/imoney/user/1"
  ```

#### ⑤ 按 id 查客户
- **`GET {BASE}/imoney/customer/{id}`**
- 鉴权：🔒 需要 token
- 路径参数：`id`（int）
- 响应：同 `④`（`imoney_user` 表，旧 user/customers 已合并为同一张表）。

#### ⑥ 新增客户（开放注册）
- **`POST {BASE}/imoney/customer/add`**
- 鉴权：🔓 开放
- 请求头：`Content-Type: application/json`
- 请求体（`ImoneyUser`，`name` 必填）：
  ```json
  { "name": "Tom", "password": "123", "gentle": "男", "email": "tom@qq.com", "city": "Beijing" }
  ```
- 成功响应（HTTP 200，裸实体，含库自增 `id`）：
  ```json
  { "id": 5, "name": "Tom", "password": "123", "gentle": "男", "email": "tom@qq.com", "city": "Beijing" }
  ```
- 校验失败（`name` 为空）：HTTP 400 + `{ "meta": { "success": false, "message": "parameter_validation_exception" }, "data": null }`

---

### 3.3 学生

> 数据模型见 §5.2 `ImoneyStudent`。

#### ⑦ 学生列表
- **`GET {BASE}/imoney/student/all`**（任意方法）
- 鉴权：🔒 需要 token
- 参数：无
- 成功响应（HTTP 200，裸数组）：
  ```json
  [{"age":20,"id":1,"name":"张三","phone":"13800000001","sex":1},
   {"age":22,"id":2,"name":"李四","phone":"13800000002","sex":0},
   {"age":21,"id":3,"name":"王五","phone":"13800000003","sex":1}]
  ```

#### ⑧ 按 id 查学生
- **`GET {BASE}/imoney/student/{id}`**（任意方法）
- 鉴权：🔒 需要 token
- 路径参数：`id`（int）
- 成功响应（HTTP 200，裸实体）：
  ```json
  {"age":20,"id":1,"name":"张三","phone":"13800000001","sex":1}
  ```
  > 查无此人返回 HTTP 200 + 空 body。

---

### 3.4 空气质量（PM2.5 / PM10 / SO2）

#### ⑨⑩⑪ 空气质量数据
- **`GET {BASE}/imoney/pm25`** ／ **`GET {BASE}/imoney/pm10`** ／ **`GET {BASE}/imoney/so2`**（任意方法）
- 鉴权：🔓 开放，但需 **token 闸门**
- 参数（Query，均选填）：

  | 参数 | 类型 | 说明 |
  |---|---|---|
  | `city` | string | 城市（当前实现不影响返回内容，预留） |
  | `token` | string | **接口令牌**，必须等于 `5j1znBVAsnSf5xQyNQyq` 才返回真实数据 |

- **token 正确**：返回对应空气质量 JSON（HTTP 200，**含 UTF-8 BOM**）：
  ```json
  [
    { "aqi": 48, "area": "上海", "pm25": 35, "pm25_24h": 20,
      "position_name": "十五厂", "primary_pollutant": null,
      "quality": "优", "station_code": "1142A", "time_point": "2019-06-07T19:00:00Z" },
    ...
  ]
  ```
- **token 错误 / 缺失**：返回 `json/error.json`（HTTP 200）：
  ```json
  { "error": "Sorry，access token invalid！" }
  ```
- 示例：
  ```bash
  curl "{BASE}/imoney/pm25?city=beijing&token=5j1znBVAsnSf5xQyNQyq"
  ```

---

### 3.5 七牛上传凭证

#### ⑫ 生成七牛 upToken
- **`POST {BASE}/imoney/QiniuUpToken`**（任意方法）
- 鉴权：🔓 开放
- 请求体：原始 JSON（客户端自带七牛凭证；后端用其本地 HMAC 计算 upToken，不落库、不打日志）：
  ```json
  { "accessKey": "你的AK", "secretKey": "你的SK", "bucket": "你的bucket" }
  ```
- **成功响应**（HTTP 200）：
  ```json
  { "upToken": "AK:xxxx:xxxx" }
  ```
- **参数不全**（任一为空）：
  ```json
  { "errorMsg": "accessKey、secretKey、bucket都不能为空！" }
  ```

---

### 3.6 投资模块（返回 HTML）

#### ⑬ 投资页片段
- **`GET {BASE}/imoney/invest`**（任意方法）
- 鉴权：🔓 开放
- **返回 `text/html;charset=UTF-8`**（非 JSON）
- 参数（Query，选填）：

  | 参数 | 类型 | 说明 |
  |---|---|---|
  | `module` | int | `0`→首页，`1`→我要投资，`2`→我的资产；不传→提示未指定；**非整数会触发 HTTP 500** |

- 响应示例（`module=1`）：
  ```html
  <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
  <HTML>
    <HEAD><TITLE>A Servlet</TITLE></HEAD>
    <BODY>
  你请求的module名称是: 我要投资
    </BODY>
  </HTML>
  ```

---

### 3.7 静态电商 / 数据 JSON（开放，原样返回）

这些接口把存在数据库（`imoney_static_resource`）里的 JSON **原样字节返回**，无需鉴权，`Content-Type: application/json;charset=UTF-8`。共两类入口：

#### A) 28 个命名端点
- **`GET {BASE}/imoney/<名称>`**（任意方法，无鉴权）
- 不存在的名称返回 **HTTP 404**。

| 接口路径 | 对应数据文件 |
|---|---|
| `GET /imoney/Accessory` | `shopping/ACCESSORY.json` |
| `GET /imoney/Bag` | `shopping/BAG.json` |
| `GET /imoney/CloseStore` | `shopping/CLOSE_STORE.json` |
| `GET /imoney/ComicStore` | `shopping/COMIC_STORE.json` |
| `GET /imoney/CosplayStore` | `shopping/COSPLAY_STORE.json` |
| `GET /imoney/Digit` | `shopping/DIGIT_URL.json` |
| `GET /imoney/DressUp` | `shopping/DRESS_UP_URL.json` |
| `GET /imoney/FoodStore` | `shopping/FOOD_STORE.json` |
| `GET /imoney/Game` | `shopping/GAME_URL.json` |
| `GET /imoney/GameStore` | `shopping/GAME_STORE.json` |
| `GET /imoney/GoodsInfo` | `shopping/GOODS_INFO.json` |
| `GET /imoney/GuFengStore` | `shopping/GUFENG_STORE.json` |
| `GET /imoney/HomePage` | `shopping/HOME_URL.json` |
| `GET /imoney/HomeProducts` | `shopping/HOME_PRODUCTS_URL.json` |
| `GET /imoney/HotPost` | `shopping/HOT_POST_URL.json` |
| `GET /imoney/JacketPage` | `shopping/JACKET_URL.json` |
| `GET /imoney/JewelryStore` | `shopping/SHOUSHI_STORE.json` ⚠️ 路径名与文件名不一致 |
| `GET /imoney/NewPost` | `shopping/NEW_POST_URL.json` |
| `GET /imoney/Overcoat` | `shopping/OVERCOAT_URL.json` |
| `GET /imoney/PantsPage` | `shopping/PANTS_URL.json` |
| `GET /imoney/SkirtPage` | `shopping/SKIRT_URL.json` |
| `GET /imoney/Stationery` | `shopping/STATIONERY_URL.json` |
| `GET /imoney/StationeryStore` | `shopping/STATIONERY_STORE.json` |
| `GET /imoney/StickStore` | `shopping/STICK_STORE.json` |
| `GET /imoney/TagPage` | `shopping/TAG_URL.json` |
| `GET /imoney/index` | `json/index.json` |
| `GET /imoney/product` | `json/product.json` |
| `GET /imoney/update` | `json/update.json` |

**典型响应结构**（以 `GET /imoney/Bag` 为例，商品分类树）：
```json
{
  "code": 200,
  "msg": "请求成功",
  "result": [
    {
      "p_catalog_id": "75",
      "parent_id": "0",
      "name": "包包",
      "pic": "",
      "is_deleted": "0",
      "child": [
        { "p_catalog_id": "76", "parent_id": "75", "name": "女包",
          "pic": "https://image.vance.xin/app/shopping/1446017977747.jpg", "is_deleted": "0" }
      ]
    }
  ]
}
```
> 各文件结构不尽相同，请以实际返回为准。所有图片外链均为 **https://image.vance.xin/...**。

#### B) 目录直出路由（按文件名取）
- **`GET {BASE}/imoney/shopping/{name}`** —— 取 `shopping/<name>`
- **`GET {BASE}/imoney/json/{name}`** —— 取 `json/<name>`
- `{name}` 需带 `.json` 后缀；不存在返回 **HTTP 404**。
- 这是 `MORE_STORE.json`、`urls_00.json`…`urls_29.json` 等**仅目录可达**文件的唯一入口。
- 示例：
  ```bash
  curl "{BASE}/imoney/shopping/MORE_STORE.json"
  curl "{BASE}/imoney/shopping/urls_00.json"
  curl "{BASE}/imoney/json/index.json"
  ```

> 完整文件清单见 **§6 附录**。

---

## 4. 快速接入流程（典型）

```text
1. 登录：POST /imoney/token/login?name=Vance&password=123
        → 取 data.token（形如 "Bearer eyJ...")
2. 调受保护接口：在请求头加 Authorization: <上一步的 token 整串>
        例：GET /imoney/user/1
3. 调开放接口：直接调用，无需 token
        例：GET /imoney/Bag、GET /imoney/pm25?token=5j1znBVAsnSf5xQyNQyq
```

---

## 5. 数据模型

### 5.1 ImoneyUser（用户 / 客户，表 `imoney_user`）
| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | int | 主键，自增（新增时无需传） |
| `name` | string | 用户名，**新增时必填**（`@NotEmpty`） |
| `password` | string | 密码（当前明文，查询响应也会带出） |
| `gentle` | string | 性别（样例值如 `男`） |
| `email` | string | 邮箱 |
| `city` | string | 城市 |

### 5.2 ImoneyStudent（学生，表 `imoney_student`）
| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | int | 主键，自增 |
| `name` | string | 姓名 |
| `age` | int | 年龄 |
| `sex` | int | 性别（`1`/`0`） |
| `phone` | string | 手机号 |

### 5.3 QiniuRequestBean（七牛请求体）
| 字段 | 类型 | 说明 |
|---|---|---|
| `accessKey` | string | 七牛 AK |
| `secretKey` | string | 七牛 SK |
| `bucket` | string | 存储空间名 |

### 5.4 Response 信封
```json
{ "meta": { "success": true, "message": "ok" }, "data": <任意/null> }
```

---

## 6. 附录：静态资源文件完整清单（共 64 个）

通过 §3.7-B 的目录路由 `/imoney/shopping/{name}`、`/imoney/json/{name}` 均可访问；其中部分另有 §3.7-A 的命名端点别名。

### shopping/（56 个）
```
ACCESSORY.json        BAG.json              CLOSE_STORE.json      COMIC_STORE.json
COSPLAY_STORE.json    DIGIT_URL.json        DRESS_UP_URL.json     FOOD_STORE.json
GAME_STORE.json       GAME_URL.json         GOODS_INFO.json       GUFENG_STORE.json
HOME_PRODUCTS_URL.json HOME_URL.json        HOT_POST_URL.json     JACKET_URL.json
MORE_STORE.json       NEW_POST_URL.json     OVERCOAT_URL.json     PANTS_URL.json
SHOUSHI_STORE.json    SKIRT_URL.json        STATIONERY_STORE.json STATIONERY_URL.json
STICK_STORE.json      TAG_URL.json
urls_00.json  urls_01.json  urls_02.json  urls_03.json  urls_04.json  urls_05.json
urls_06.json  urls_07.json  urls_08.json  urls_09.json  urls_10.json  urls_11.json
urls_12.json  urls_13.json  urls_14.json  urls_15.json  urls_16.json  urls_17.json
urls_18.json  urls_19.json  urls_20.json  urls_21.json  urls_22.json  urls_23.json
urls_24.json  urls_25.json  urls_26.json  urls_27.json  urls_28.json  urls_29.json
```

### json/（8 个）
```
error.json   index.json   login.json   pm10.json
pm25.json    product.json so2.json     update.json
```

---

## 7. 备注（对接注意点小结）

1. **成功 vs 失败的结构不同**：`user/customer/student` 查询**成功**是裸 JSON，**失败**（401/500 等）是 `{meta,data}` 信封 —— 先看 HTTP 状态码再解析。
2. **token 整串含 `Bearer `**：登录返回的 `data.token` 已带前缀，放进 `Authorization` 头时**不要再加** `Bearer `。
3. **BOM**：`index.json`、`product.json`、`update.json`、`pm25/pm10/so2.json` 等带 UTF-8 BOM，部分严格 JSON 解析器需用 `utf-8-sig` 或先剥 BOM。
4. **图片链接**：全部为 `https://image.vance.xin/...`（已统一为 https）。
5. **`password` 外泄**：用户查询响应当前会带出 `password` 字段，APP 端请忽略，勿展示。
6. **公网入口**：`https://api.vance.xin`，路由已开通（见 §0）。

---

*文档生成日期：2026-06-09 ｜ 对应服务版本：v1.0.8 ｜ 来源：后端源码逐接口核对 + 生产环境实测响应*
