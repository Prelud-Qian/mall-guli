# CLAUDE.md

Claude 在此项目中工作的指引文件。

## 项目概述

`mall-platform` 是一个全栈商城项目，monorepo 结构：

- `backend/` — Spring Cloud 微服务（Spring Boot 2.6.6 + Spring Cloud 2021.0.5 + Spring Cloud Alibaba 2021.0.5.0）
- `frontend/` — Vue 2 + Element UI 管理后台（renren-fast-vue）

## 构建与运行

```bash
# 后端（Maven，Java 8）
cd backend
mvn clean install -DskipTests

# 前端
cd frontend
npm install
npm run dev
```

## 后端模块

| 模块 | 端口 | 包名 | 职责 |
|------|------|------|------|
| `mall-common` | — | `com.constant.*`、`com.mall.common.utils.R` | 通用类：异常枚举、常量、工具类、R 响应体 |
| `mall-gateway` | 88 | `com.mall.mall_gateway` | Spring Cloud Gateway 网关，统一入口 |
| `mall-auth-server` | 20000 | `com.example.mall_auth_server` | 认证服务：登录、注册、短信验证码 |
| `mall-product` | 10000 | `com.mall.product` | 商品服务：SPU/SKU CRUD、首页/详情页模板 |
| `mall-member` | 8000 | `com.mall.member` | 会员服务：注册、登录校验 |
| `mall-cart` | 40000 | `com.example.mall_cart` | 购物车服务：Redis 存储 + 拦截器传递用户信息 |
| `mall-coupon` | 7000 | `com.mall.mall_coupon` | 优惠券服务 |
| `mall-ware` | 11000 | `com.mall.mall_ware` | 仓储服务 |
| `mall-search` | 12000 | `com.example.mall_search` | 检索服务（Elasticsearch） |
| `mall-third-party-service` | 30000 | `com.example.mall_third_party_service` | 第三方服务（短信等） |
| `mall-order` | 9010 | `com.mall.mall_order`（主类）、`com.mall.order.order`（业务） | 订单服务：结算/确认页、Feign 调购物车与会员 |
| `renren-fast` | — | `io.renren` | 后台 API（人人开源） |
| `renren-generator` | — | `io.renren` | 代码生成器（人人开源） |

## 代码约定

- Java 8，包名按模块分：`com.example.<module_name>`（新模块 `mall-product`/`mall-member` 等用 `com.mall.*`，两者共存）
- 通用类放 `mall-common`：`com.constant.*`、`com.exception.*`、`com.vo.*`、`com.mall.common.utils.R`
- Feign 接口放调用方模块的 `feign/` 目录下
- Thymeleaf 模板放 `src/main/resources/templates/`
- 静态资源经网关路由，各服务静态目录独立（如 `/static/cart/`）

## 关键架构

### 跨子域 Session 共享

`mall-auth-server`、`mall-product`、`mall-cart`、`mall-order` 均配置 `MallSessionConfig`：

- `@EnableRedisHttpSession` + `GenericJackson2JsonRedisSerializer`
- Cookie 名 `GULISESSION`，域 `mall.com`
- 登录后 session 属性 key 为 `loginUser`（`AuthServerConstant.LOGIN_USER`），值为 `MemberResponseVo`
- Thymeleaf 模板中可直接用 `${session.loginUser}` 读取
- 给新模块接入时：pom 加 `spring-session-data-redis`，复制该 Config 即可

### 购物车用户识别链路

```
CartInterceptor.preHandle
  ├─ session.loginUser 存在 → userId（登录用户）
  ├─ user-key cookie 存在 → userKey（临时身份）
  ├─ user-key 不存在 → 生成 UUID（首次访问）
  └─ tempUser = (userId == null)
CartInterceptor.postHandle
  └─ 临时用户 → 写回/续期 user-key cookie（30 天）
Controller 取用
  └─ CartInterceptor.threadLocal.get() → UserInfoTo
```

### 订单结算链路

```
购物车 cartList.html「去结算」→ order.mall.com/toTrade
OrderWebController.toTrade
  └─ LoginUserInterceptor 拦截（拦截所有 /**）
       ├─ 已登录 → OrderServiceImpl.confirmOrder()（线程池异步 Feign 查地址+购物车）→ confirm.html
       └─ 未登录 → redirect auth.mall.com/login.html?redirect_url=<原页面>
            → 登录成功后按 redirect_url 回跳（LoginController 校验以 http://order.mall.com 开头）
Feign 跨服务识别用户
  └─ FeignConfig RequestInterceptor 把当前请求的 Cookie 同步到远程调用
```

### 线程池与请求上下文

`RequestContextHolder` 基于 ThreadLocal，线程池子线程拿不到请求上下文：

- 异步任务里调 Feign 前，需先 `RequestContextHolder.setRequestAttributes(主线程捕获的 attrs)`
- FeignConfig 的 Cookie 同步拦截器对 null 做了防御（无上下文直接跳过）

### 网关路由

`mall-gateway/src/main/resources/application.yml`：

- 域名路由（Host 谓词）：`mall.com` → product，`auth.mall.com` → auth，`cart.mall.com` → cart，`search.mall.com` → search，`order.mall.com` → order
- 接口路由（Path 谓词）：`/api/**` → renren-fast，`/api/product/**` → product 等
- 前端请求先到 gateway 再分发到各微服务

## 注意事项

- 根目录 `backend/` 不是 Maven 父项目，父 POM 就是 `backend/pom.xml`
- 模块间的 path 可能有大小写不一致（如 `com/constant/` vs `com/mall/`），不影响 Java 编译
- Thymeleaf 表达式中 `&&` 和 `||` 不能用，用 `and` 和 `or` 替代
- `mall-cart` 不依赖数据库：`@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})`——`mall-common` 传递引入了 Druid，两个都要排除，只排 Spring Boot 的没用
- Nacos 注册 IP 与容器绑定地址不一致时（网关 500、直连 200 的典型症状），在 `application.properties` 加 `spring.cloud.nacos.discovery.ip` 显式指定
- `renren-fast` 被 `mall-common` 间接引入，其 `application.yml` 的 `context-path: /renren-fast` 会污染各微服务，需在自身配置显式 `server.servlet.context-path=/`
- 各微服务 `application.properties` 端口：gateway 88、auth 20000、product 10000、member 8000、coupon 7000、ware 11000、cart 40000、search 12000、third-party 30000、order 9010
- mall-order 包结构特殊（主类包 `com.mall.mall_order` ≠ 业务包 `com.mall.order.order`）：`@EnableFeignClients` 必须显式 `basePackages = "com.mall.order.order.feign"`，否则 Feign bean 找不到
- 改了不重启不生效：网关路由改动需重启 mall-gateway；mall-cart 没有 devtools，Thymeleaf 模板改动必须重启服务
- Windows 环境文件统一 CRLF 行尾；Git 默认会提示 LF→CRLF 转换警告，属正常
- 数据库：`mall_pms`（商品）、`mall_ums`（会员）、`mall_sms`（营销）、`mall_wms`（仓储）、`mall_oms`（订单）
