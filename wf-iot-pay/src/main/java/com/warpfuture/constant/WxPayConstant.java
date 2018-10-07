package com.warpfuture.constant;

/** Created by fido on 2018/4/18. */
public class WxPayConstant {
  public static final String dataNotifyURL =
      "http://pay-gateway.warpfuture.com/wxpay/callBack"; // 支付结果回调地址
  public static final String refundNotifyURL =
      "http://pay-gateway.warpfuture.com/wxpay/refundBack"; // 退款结果回调地址
  public static final String quaryOrderURL =
      "https://api.mch.weixin.qq.com/pay/orderquery"; // 主动查询微信支付服务器订单消息的地址

  public static final String QUERY_REFUND_URL =
      "https://api.mch.weixin.qq.com/pay/refundquery"; // 主动查询微信支付服务器退款的地址

  public static final String wxCreateOrderURL =
      "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 微信服务器统一下单地址

  public static final String WX_REFUND_URL =
      "https://api.mch.weixin.qq.com/secapi/pay/refund"; // 微信服务器退款地址

  public static final String RETURN_CODE_SUCCESS = "SUCCESS"; // 通信结果

  public static final String RESULT_CODE_SUCCESS = "SUCCESS"; // 业务结果

  public static final String REFUND_STATUS_SUCCESS = "SUCCESS"; // 退款成功

  public static final String REFUND_STATUS_CHANGE = "CHANGE"; // 退款异常

  public static final String REFUND_STATUS_REFUNDCLOSE = "REFUNDCLOSE"; // 退款关闭

  public static final String WX_PAYTYPE_JS = "JSAPI"; // JS支付方式

  public static final String WX_SIGN_TYPE = "MD5"; // 签名方式

  public static final String REFUND_ACCOUNT_RECHARGE = "REFUND_SOURCE_RECHARGE_FUNDS"; // 可用余额退款

  public static final String REFUND_ACCOUNT_UNSETTLED = "REFUND_SOURCE_UNSETTLED_FUNDS"; // 未结算资金退款
}
