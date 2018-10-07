package com.warpfuture.constant;

/** Created by fido on 2018/5/16. */
public class OrderConstant {
  public static final Integer ORDER_STATUS_READY = 0; // 初始化订单
  public static final Integer ORDER_STATUS_PAY = 1; // 已支付
  public static final Integer ORDER_STATUS_REFUND = 2; // 已退款
  public static final Integer ORDER_STATUS_CANCEL = 3; // 已经取消
  public static final Integer ORDER_STATUS_REQUIRE_REFUND = 4; // 申请退款中
  public static final Integer ORDER_STATUS_OVERTIME = 5; // 因超时取消
  public static final Integer ORDER_WXTRADE = 0; // 微信支付
  public static final Integer ORDER_ALITRADE = 1; // 支付宝支付
  public static final Integer NOTIFY_UNIFIEDORDER = 0; // 支付回调
  public static final Integer NOTIFY_REFUND = 1; // 退款回调
  public static final Long ORDER_OVER_TIME = 30000L; // 回调超时时间
}
