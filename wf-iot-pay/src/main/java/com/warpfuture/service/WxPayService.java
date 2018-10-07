package com.warpfuture.service;

import com.warpfuture.dto.OrderOperationDto;
import com.warpfuture.entity.order.Order;

import java.util.Map;

/** Created by fido on 2018/5/16. */
public interface WxPayService {
  /**
   * 统一下单
   *
   * @param orderOperationDto
   * @return
   */
  public Map<String, String> preparCreateOrder(OrderOperationDto orderOperationDto);

  /**
   * 接收微信支付服务器回调的结果
   *
   * @param xml
   * @return
   */
  public String notifyPayment(String xml);

  /**
   * 主动查询微信支付服务器的数据包
   *
   * @param merchantId
   * @param merchantTradeNumber
   * @return
   */
  public String prepareQueryData(String merchantId, String merchantTradeNumber);



  /**
   * 申请退款
   *
   * @param orderOperationDto
   * @return
   */
  public Order refund(OrderOperationDto orderOperationDto);

  /**
   * 退款结果回调
   *
   * @param xml
   * @return
   */
  public String notifyRefund(String xml);
}
