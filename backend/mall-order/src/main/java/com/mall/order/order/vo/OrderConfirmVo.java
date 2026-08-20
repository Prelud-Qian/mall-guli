package com.mall.order.order.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单确认页需要用的数据
 */
@Data
public class OrderConfirmVo {

    // 收货地址
    @Setter @Getter
    List<MemberAddressVo> address;

    // 所有选中的购物项
    @Setter @Getter
    List<OrderItemVo> items;

    // 发票记录。。。

    // 优惠券信息。。。

    // 积分
    @Setter @Getter
    private Integer integration;

    // 防重令牌
    @Setter @Getter
    String orderToken;

    // 订单总额
//    BigDecimal total;

    public BigDecimal getTotal() {
        BigDecimal sum = new BigDecimal("0");
        if (items != null){
            for (OrderItemVo item : items) {
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                sum = sum.add(multiply);
            }
        }
        return sum;
    }

    // 应付价格
//    BigDecimal payPrice;

    public BigDecimal getPayPrice() {
        return getTotal();
    }
}
