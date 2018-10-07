package com.warpfuture.repository;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.order.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/18. */
@Repository
public interface OrderRepository {

  /**
   * 根据商户id分页查找订单
   *
   * @param merchantId
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public PageModel<Order> findByMerchantId(String merchantId, Integer pageIndex, Integer pageSize);

  /**
   * 根据商户id和商户的交易订单号查找订单
   *
   * @param merchantId
   * @param merchantTradeNumber
   * @return
   */
  public Order findByMerchantTradeNumber(String merchantId, String merchantTradeNumber);

}
