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

| 模块 | 职责 |
|------|------|
| `mall-common` | 通用类：异常枚举、常量、工具类、R 响应体 |
| `mall-gateway` | Spring Cloud Gateway 网关，统一入口 |
| `mall-auth-server` | 认证服务：登录、注册、短信验证码 |
| `mall-product` | 商品服务：商品 CRUD、详情页模板 |
| `mall-member` | 会员服务：会员注册、登录 |
| `mall-cart` | 购物车服务：Redis 存储 + ThreadLocal 传递用户信息 |
| `mall-coupon` | 优惠券服务 |
| `mall-order` | 订单服务 |
| `mall-ware` | 仓储服务 |
| `mall-search` | 检索服务（Elasticsearch） |
| `mall-third-party-service` | 第三方服务（短信等） |
| `renren-fast` | 后台 API（人人开源） |
| `renren-generator` | 代码生成器（人人开源） |

## 代码约定

- Java 8，包名按模块分：`com.example.<module_name>`
- 通用类放 `mall-common`：`com.constant.*`、`com.exception.*`、`com.mall.common.utils.R`
- Feign 接口放调用方模块的 `feign/` 目录下
- 模板文件（Thymeleaf）放 `src/main/resources/templates/`
- 前端页面路由：`auth.mall.com` → 认证页面，`mall.com` → 商城首页

## 注意事项

- 模块间的 path 可能有大小写不一致（如 `com/constant/` vs `com/mall/`），不影响 Java 编译
- 根目录 `backend/` 不是 Maven 父项目，父 POM 就是 `backend/pom.xml`
- `mall-gateway` 做路由转发，前端请求先到 gateway 再分发到各微服务
- Thymeleaf 表达式中 `&&` 和 `||` 不能用，用 `and` 和 `or` 替代
- 跨子域 Session 共享：`mall-auth-server` 和 `mall-cart` 均需配置 Spring Session Redis，cookie 域统一设为 `mall.com`，序列化器统一用 `GenericJackson2JsonRedisSerializer`
- `mall-cart` 不依赖数据库，需排除 `DataSourceAutoConfiguration` 和 `DruidDataSourceAutoConfigure`
- Nacos 注册 IP 如与容器绑定地址不一致，通过 `spring.cloud.nacos.discovery.ip` 显式指定
