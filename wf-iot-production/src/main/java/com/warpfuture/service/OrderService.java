package com.warpfuture.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.order.Order;

/** Created by fido on 2018/5/15. */
public interface OrderService {
  /**
   * 查询订单
   *
   * @param accountId
   * @param merchantTradeNumber
   * @param merchantId
   * @return
   */
  public Order query(String accountId, String merchantTradeNumber, String merchantId);

  /**
   * 查询商户订单
   *
   * @param accountId
   * @param merchantId
   * @param pageSize
   * @param pageIndex
   * @return
   */
  public PageModel<Order> queryList(
          String accountId, String merchantId, Integer pageSize, Integer pageIndex);
}
