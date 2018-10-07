package com.warpfuture.repository;

import com.warpfuture.entity.order.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/18. */
@Repository
public interface OrderRepository {

  /**
   * 根据商户id和商户的交易订单号查找订单
   *
   * @param merchantId
   * @param merchantTradeNumber
   * @return
   */
  public Order findByMerchantTradeNumber(String merchantId, String merchantTradeNumber);

  /**
   * 新增订单
   *
   * @param order
   */
  public void insert(Order order);

  /**
   * 更新订单
   *
   * @param order
   */
  public void save(Order order);

  /**
   * 根据云端订单id查找订单
   *
   * @param orderId
   * @return
   */
  public Order findById(String orderId);

  /**
   * 得到尚未完成的订单
   *
   * @return
   */
  public List<Order> getPreparToQueryList();

  /**
   * 得到超时的订单
   *
   * @return
   */
  public List<Order> getOverTimeWxQrder();

  /**
   * 得到超时的退款申请
   *
   * @return
   */
  public List<Order> getOverTimeWxRefund();
}
