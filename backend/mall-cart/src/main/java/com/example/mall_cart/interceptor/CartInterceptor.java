package com.example.mall_cart.interceptor;

import com.constant.AuthServerConstant;
import com.constant.CartConstant;
import com.example.mall_cart.vo.UserInfoTo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

import com.xunqi.common.vo.MemberResponseVo;
import org.springframework.web.servlet.ModelAndView;

public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    /**
     * 临时用户：
     *   - 数据在 Cookie 里（user-key）
     *   - Cookie 过期 = 数据丢失 ❌
     *   - 必须刷新 Cookie 来保活
     *
     * 登录用户：
     *   - 数据在服务端（Session/Redis）
     *   - Cookie 只是 Session ID（钥匙）
     *   - 钥匙过期了？去服务端续期就行 ✅
     *   - 不需要频繁刷新 Cookie
     *   因为登录用户有 userId，所以根本不需要依赖 Cookie 来识别身份！
     */

    /**
     * 目标方法执行之前拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        // 服务器里有非常多 Session，每个在线用户都有一个独立的 Session。
        // 从请求中获取 HttpSession  如果不存在，会自动创建
        HttpSession session = request.getSession();
        // Session 确实需要 Session ID 来取数据。但这段代码中 session.getAttribute() 不需要你手动传 Session ID，是因为 Spring 已经帮你做好了"自动匹配"。
        MemberResponseVo member = (MemberResponseVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (member != null) {
            // 用户登录
            userInfoTo.setUserId(member.getId());
        }

        // 从 HTTP 请求中获取所有 Cookie 数组
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_COOKIE_NAME)) {
                    userInfoTo.setUserKey(cookie.getValue());
                }
            }
        }

        // 第一次访问，没有 user-key，分配一个临时用户标识
        if (userInfoTo.getUserKey() == null) {
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }

        // 标记是否为临时用户（未登录）
        userInfoTo.setTempUser(userInfoTo.getUserId() == null);

        // 目标方法执行之前
        threadLocal.set(userInfoTo);

        return true;
    }

    /**
     * 业务执行之后
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        if (userInfoTo.isTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("mall.com");
            cookie.setPath("/");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
