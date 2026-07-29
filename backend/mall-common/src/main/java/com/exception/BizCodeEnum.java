package com.exception;

/***
 * 错误码和错误信息定义类
 * 1. 错误码定义规则为5为数字
 * 2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
 * 3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
 * 错误码列表：
 * 10: 通用
 *    001: 参数格式校验
 * 11: 商品
 * 12: 订单
 * 13: 购物车
 * 14: 物流
 *
 */

/**
 * 枚举类
 * enum 本质是特殊 class，默认 final、不能 new 对象
 */
public enum BizCodeEnum {

    // 枚举实例（常量对象）
    // 枚举常量 是public static final全局唯一实例，类加载时就创建好
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),

    // 仓储采购模块
    PURCHASE_MERGE_FAIL(16001, "合并失败：仅【新建】状态采购单、新建状态采购需求允许合并"),

    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    SMS_CODE_EXCEPTION(10002, "验证码获取频率太高，稍后再试"),
    USER_EXIST_EXCEPTION(15001, "用户存在"),
    PHONE_EXIST_EXCEPTION(15002, "手机号存在"),
    LOGINACCOUNT_PASSWORD_INVALID_EXCEPTION(15003, "账号密码错误");

    private int code;
    private String msg;

    // 构造方法私有：BizCodeEnum(int code,String msg)默认 private，不能手动 new BizCodeEnum ()；
    BizCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
