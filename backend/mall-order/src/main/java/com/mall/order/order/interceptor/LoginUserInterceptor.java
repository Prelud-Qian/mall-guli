package com.mall.order.order.interceptor;

import com.constant.AuthServerConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunqi.common.vo.MemberResponseVo;

@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MemberResponseVo attribute = (MemberResponseVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute != null) {
            loginUser.set(attribute);
            return true;
        } else {
            // 没登录就去登录
            request.getSession().setAttribute("msg", "请先进行登录");
            response.sendRedirect("http://auth.mall.com/login.html?redirect_url="
                    + URLEncoder.encode(request.getRequestURL().toString(), "UTF-8"));
            return false;
        }
    }
}
