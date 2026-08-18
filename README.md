# mall-platform

全栈微服务电商平台，基于 Spring Cloud Alibaba 构建。

## 系统架构

```
浏览器（mall.com 及各子域名）
        │
        ▼
┌───────────────────┐
│   mall-gateway    │  统一入口 :88，按 Host/Path 路由到各微服务
└────────┬──────────┘
         │
    ┌────┴───────────────────────────────────┐
    │              Nacos 注册中心             │
    └────┬───────────────────────────────────┘
         │
 ┌───────┼──────────┬──────────┬──────────────┐
 ▼       ▼          ▼          ▼              ▼
┌──────────┐  ┌──────────┐  ┌─────────┐  ┌──────────┐
│mall-product│ │mall-search│ │mall-cart│ │mall-auth │  ...
└────┬─────┘  └────┬─────┘  └────┬────┘  └────┬─────┘
     │             │             │            │
     ▼             ▼             ▼            ▼
┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
│  MySQL   │  │Elasticsearch│ │  Redis   │  │  Redis   │
└──────────┘  └──────────┘  │(购物车+Session)│ └──────────┘
                            └──────────┘
```

## 项目结构

```
mall-platform/
├── backend/                            # 后端微服务（Spring Cloud）
│   ├── mall-common/                    # 通用模块：常量、异常、工具类、R 响应体
│   ├── mall-gateway/                   # API 网关（Spring Cloud Gateway）
│   ├── mall-auth-server/               # 认证服务：登录、注册、短信验证码
│   ├── mall-product/                   # 商品服务：SPU/SKU、品牌、分类、属性
│   ├── mall-member/                    # 会员服务：注册、登录、积分
│   ├── mall-coupon/                    # 优惠券服务
│   ├── mall-cart/                      # 购物车服务：Redis 存储 + Spring Session
│   ├── mall-order/                     # 订单服务
│   ├── mall-ware/                      # 仓储服务：库存管理
│   ├── mall-search/                    # 检索服务（Elasticsearch）
│   ├── mall-third-party-service/       # 第三方服务（短信、OSS）
│   ├── renren-fast/                    # 后台管理系统 API
│   ├── renren-generator/               # 代码生成器
│   └── pom.xml                         # 父 POM
│
└── frontend/                           # 前端管理后台（Vue 2 + Element UI）
    ├── src/                            # 源码
    ├── build/                          # Webpack 构建配置
    └── config/                         # 环境配置
```

## 微服务清单

| 模块 | 端口 | 职责 |
|------|------|------|
| `mall-gateway` | 88 | 统一入口，按 Host/Path 路由 |
| `mall-auth-server` | 20000 | 登录、注册、短信验证码、Spring Session 写入 |
| `mall-product` | 10000 | SPU/SKU、品牌、分类、属性管理，商城首页/详情页 |
| `mall-member` | 8000 | 会员注册、登录校验 |
| `mall-coupon` | 7000 | 优惠券 |
| `mall-ware` | 11000 | 库存管理 |
| `mall-cart` | 40000 | 购物车（Redis），临时用户标识，登录合并 |
| `mall-search` | 12000 | 商品检索（Elasticsearch） |
| `mall-third-party-service` | 30000 | 短信等第三方接口 |
| `mall-order` | — | 订单（待开发） |

## 技术栈

| 层 | 技术 | 版本 |
|---|------|------|
| 框架 | Spring Boot | 2.6.6 |
| 微服务 | Spring Cloud | 2021.0.5 |
| 微服务 | Spring Cloud Alibaba | 2021.0.5.0 |
| 注册/配置 | Nacos | 1.x |
| 网关 | Spring Cloud Gateway | — |
| ORM | MyBatis-Plus | 3.x |
| 缓存 | Redis（Lettuce/Jedis） | — |
| 会话 | Spring Session Data Redis | — |
| 搜索 | Elasticsearch | 7.x |
| 远程调用 | OpenFeign | 3.1.0 |
| 模板引擎 | Thymeleaf | — |
| 语言 | Java | 8 |
| 前端框架 | Vue | 2.5 |
| 前端 UI | Element UI | 2.8 |

## 快速开始

### 环境要求

| 依赖 | 版本/说明 |
|------|-----------|
| JDK | 8 |
| Maven | 3.6+ |
| Node.js | 12+（仅前端） |
| MySQL | 5.7+，库：`mall_pms` / `mall_ums` / `mall_sms` / `mall_wms` / `mall_oms` |
| Redis | 供购物车与跨服务 Session 使用 |
| Nacos | 注册中心 + 配置中心，`127.0.0.1:8848` |
| Elasticsearch | 商品索引 `mall_product` |

### hosts 配置

```hosts
127.0.0.1 mall.com
127.0.0.1 auth.mall.com
127.0.0.1 search.mall.com
127.0.0.1 item.mall.com
127.0.0.1 member.mall.com
127.0.0.1 cart.mall.com
127.0.0.1 order.mall.com
```

### 默认账号

| 系统 | 用户名 | 密码 |
|------|--------|------|
| 电商网站 | `admin` | `admin123` |
| 后台管理系统 | `admin` | `admin` |

### 启动后端

```bash
cd backend
mvn clean install -DskipTests

# 启动顺序
# 1. Nacos、MySQL、Redis、Elasticsearch
# 2. mall-gateway
# 3. mall-product → mall-member → mall-cart → mall-search → mall-auth-server
# 4. 其余服务按需启动
```

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

## 域名规划

| 域名 | 服务 | 说明 |
|------|------|------|
| `mall.com` | mall-product | 商城首页 |
| `auth.mall.com` | mall-auth-server | 认证中心（登录/注册） |
| `search.mall.com` | mall-search | 商品搜索 |
| `item.mall.com` | mall-product | 商品详情 |
| `member.mall.com` | mall-member | 会员中心 |
| `cart.mall.com` | mall-cart | 购物车 |
| `order.mall.com` | mall-order | 订单 |

## 核心功能

### 跨子域 Session 共享

`mall-auth-server`、`mall-product`、`mall-cart` 均接入 Spring Session Redis：

- Cookie 名统一 `GULISESSION`，域统一 `mall.com`（跨子域可见）
- 序列化统一 `GenericJackson2JsonRedisSerializer`（JSON 格式）
- 登录后任意子域可读 `session.loginUser`（`MemberResponseVo`）

### 购物车用户识别

- 未登录：`user-key` cookie（UUID，30 天）作为临时身份，购物车存 Redis
- 已登录：`userId` 来自 Session；临时购物车自动合并到登录购物车
- `CartInterceptor` 将用户信息封装进 `ThreadLocal<UserInfoTo>`，Controller 直接取用

### 商品检索

- 上架商品同步至 ES 索引 `mall_product`
- `search.mall.com/list.html` 支持：关键字、三级分类、品牌、属性、价格区间、排序、分页、高亮、聚合筛选

## 开发进度

- [x] 商品服务（SPU/SKU CRUD + 详情页 + 首页分类）
- [x] 检索服务（ES 同步 + 检索页）
- [x] 认证服务（登录/注册/短信 + Spring Session 共享）
- [x] 会员服务（注册、登录校验）
- [x] 购物车（增删改查、选中、临时用户、登录合并）
- [ ] 订单服务
- [ ] 支付
- [ ] 仓储（库存锁定）
- [ ] 优惠券（领取、使用）
