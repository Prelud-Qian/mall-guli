package com.mall.mall_gateway;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 开启服务注册发现
 */
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,    // 排除数据库
		DruidDataSourceAutoConfigure.class    // 排除 Druid
})
@EnableDiscoveryClient
public class MallGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallGatewayApplication.class, args);
	}

}
