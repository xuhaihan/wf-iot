package com.warpfuture.dto;

import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.order.Order;
import lombok.Data;
import lombok.ToString;

/** Created by fido on 2018/5/17. 确定通知次数，若未收到业务服务器接收回来的成功响应，则需要重发 */
@ToString
@Data
public class NotifyStreamInfoDto {
  private Merchant merchant; // 需要发送的商户信息
  private Integer result; // 0:失败，1:成功
  private String mesg; // 错误原因
  private Integer nums; // 本次属于第几次发送
  private Order order; // 本次需要回调通知的订单信息
  private Long time; // 通知时间
  private Integer tradeType; // 0：微信 1：支付宝
  private Integer notifyType; // 0：支付 1：退款
}
