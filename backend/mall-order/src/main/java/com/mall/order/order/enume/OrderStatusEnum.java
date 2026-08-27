package com.mall.order.order.enume;

public enum OrderStatusEnum {
    CREATE_NEW(0, "待付款"),
    PAYED(1, "已付款"),
    SENTED(2, "已发货"),
    RECEIVED(3, "已完成"),
    CANCELED(4, "已取消"),      // 修正拼写
    SERVICING(5, "售后中"),
    SERVICED(6, "售后完成");

    private final Integer code;  // 建议加上 final
    private final String msg;    // 建议加上 final

    OrderStatusEnum(Integer code, String msg) {
        if (code == null || msg == null) {
            throw new IllegalArgumentException("code 和 msg 不能为空");
        }
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    // 根据 code 获取枚举
    public static OrderStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.msg;
    }
}