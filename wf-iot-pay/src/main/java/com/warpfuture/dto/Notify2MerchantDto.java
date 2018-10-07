package com.warpfuture.dto;

import com.warpfuture.entity.order.Order;
import lombok.Data;
import lombok.ToString;

/** Created by fido on 2018/5/17. 回调给业务服务器的通知 */
@ToString
@Data
public class Notify2MerchantDto {
  private Order order; // 需要返回给商家的通知
  private Integer tradeType; // 支付类型:微信，支付宝
  private Integer notifyType; // 回调通知类型:请求交易，退款
}
