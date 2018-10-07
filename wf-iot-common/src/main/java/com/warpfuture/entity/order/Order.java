package com.warpfuture.entity.order;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Created by fido on 2018/4/18. 订单类 */
@Document(collection = "iot_order_info")
@Data
@ToString
public class Order {
  private String accountId; // 账户Id
  private String merchantId; // 商户Id
  @Id private String orderId; // 订单记录在云端的id号
  private Integer status; // 订单状态 0:未支付 1:已支付 2:已退款 3:已取消 4:申请退款中,5:超时未收到通知改为取消
  private Integer tradeType; // 交易类型,0:微信 1:支付宝
  private String merchantTradeNumber; // 商户订单号
  private OrderData orderData; // 具体的订单详情数据
  private Long createTime; // 订单创建时间
  private Long updateTime; // 订单更新时间
}
