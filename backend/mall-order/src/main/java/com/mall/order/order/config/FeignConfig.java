package com.mall.order.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes == null) {
                    // 没有 web 请求上下文（如线程池子线程未传递），直接跳过
                    return;
                }
                HttpServletRequest request = requestAttributes.getRequest();
                // 同步请求头数据  Cookie
                String cookie = request.getHeader("Cookie");
                // 给新请求同步了老请求的 Cookie
                if (cookie != null) {
                    requestTemplate.header("Cookie", cookie);
                }
            }
        };
    }
}
