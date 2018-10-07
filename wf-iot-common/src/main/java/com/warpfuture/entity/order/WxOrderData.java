package com.warpfuture.entity.order;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/** Created by fido on 2018/4/18. */
@ToString
@Data
public class WxOrderData extends OrderData {
  private String wxTradeNumber; // 微信订单号
  private String wxOpenId; // 终端用户的openId
  private String wxPayTradeType; // 交易类型,H5,JS..
  private Map<String, Object> extensions; // 微信支付的其余属性
}
