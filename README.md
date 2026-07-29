# mall-platform

谷粒商城 — 全栈微服务电商平台，基于 Spring Cloud Alibaba 构建。

## 项目结构

```
mall-platform/
├── backend/                          # 后端微服务（Spring Cloud）
│   ├── mall-common/                  # 通用模块：常量、异常、工具类、R 响应体
│   ├── mall-gateway/                 # API 网关（Spring Cloud Gateway）
│   ├── mall-auth-server/             # 认证服务：登录、注册、短信验证码
│   ├── mall-product/                 # 商品服务：SPU/SKU、品牌、分类、属性
│   ├── mall-member/                  # 会员服务：注册、登录、积分
│   ├── mall-coupon/                  # 优惠券服务
│   ├── mall-order/                   # 订单服务
│   ├── mall-ware/                    # 仓储服务：库存管理
│   ├── mall-search/                  # 检索服务（Elasticsearch）
│   ├── mall-third-party-service/     # 第三方服务（短信、OSS）
│   ├── renren-fast/                  # 后台管理系统 API
│   ├── renren-generator/             # 代码生成器
│   └── pom.xml                       # 父 POM
│
└── frontend/                         # 前端管理后台（Vue 2 + Element UI）
    ├── src/                          # 源码
    ├── build/                        # Webpack 构建配置
    └── config/                       # 环境配置
```

## 技术栈

| 层 | 技术 | 版本 |
|---|------|------|
| 框架 | Spring Boot | 2.6.6 |
| 微服务 | Spring Cloud | 2021.0.5 |
| 微服务 | Spring Cloud Alibaba | 2021.0.5.0 |
| 注册/配置 | Nacos | — |
| 网关 | Spring Cloud Gateway | — |
| ORM | MyBatis-Plus | — |
| 缓存 | Redis | — |
| 搜索 | Elasticsearch | — |
| 模板引擎 | Thymeleaf | — |
| 语言 | Java | 8 |
| 前端框架 | Vue | 2.5 |
| 前端 UI | Element UI | 2.8 |

## 快速开始

### 环境要求

- JDK 8
- Maven 3.6+
- Node.js 12+
- MySQL 5.7+
- Redis
- Nacos

### 后端

```bash
cd backend

# 编译全部模块
mvn clean install -DskipTests

# 按顺序启动（建议先启 Nacos，再启各服务）
# mall-gateway → mall-product → mall-member → mall-coupon → mall-order → mall-ware → mall-search → mall-third-party-service → mall-auth-server
```

### 前端

```bash
cd frontend
npm install
npm run dev
```

## 域名规划

| 域名 | 服务 |
|------|------|
| `mall.com` | 商城首页 |
| `auth.mall.com` | 认证中心（登录/注册） |
| `search.mall.com` | 商品搜索 |
| `item.mall.com` | 商品详情 |
| `member.mall.com` | 会员中心 |
| `cart.mall.com` | 购物车 |
| `order.mall.com` | 订单 |

## 开发进度

- [x] 商品服务（CRUD + 详情页）
- [x] 检索服务（Elasticsearch）
- [x] 认证服务（登录/注册页面）
- [ ] 购物车
- [ ] 订单
- [ ] 支付
