package com.example.mall_cart.controller;

import com.constant.AuthServerConstant;
import com.example.mall_cart.interceptor.CartInterceptor;
import com.example.mall_cart.vo.CartItem;
import com.example.mall_cart.vo.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    /**
     * 浏览器有一个cookie；user-key；标识用户身份，一个月后过期；
     * 如果第一次使用id的购物车功能，都会给一个临时的用户身份；
     * 浏览器以后保存，每次访问都会带上这个cookie；
     *
     * 登录：session有
     * 没登录：按照cookie里面带来user-key来做。
     * 第一次：如果没有临时用户，帮忙创建一个临时用户。
     * @param
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage() {

        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println(userInfoTo);

        return "cartList";
    }

}
