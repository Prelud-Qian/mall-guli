package com.mall.order.order.web;

import com.mall.order.order.service.OrderService;
import com.mall.order.order.vo.OrderConfirmVo;
import com.mall.order.order.vo.OrderSubmitVo;
import com.mall.order.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();

        model.addAttribute("orderConfirmData", confirmVo);
        return "confirm";
    }

    /**
     * 下单功能
     * @param vo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo){
        System.out.println("订单提交的数据..." + vo);

        SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
        if (responseVo.getCode() == 0){
            // 成功
            // 下单成功来到支付选择页
            return "pay";
        }else {
            // 下单失败回到订单确认页重新确认订单信息
            return "redirect:http://order.mall.com/toTrade";
        }

    }
}
