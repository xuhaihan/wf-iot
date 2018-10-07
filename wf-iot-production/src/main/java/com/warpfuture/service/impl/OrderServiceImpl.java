package com.warpfuture.service.impl;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.order.Order;
import com.warpfuture.repository.MerchantRepository;
import com.warpfuture.repository.OrderRepository;
import com.warpfuture.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by fido on 2018/5/15. */
@Service
public class OrderServiceImpl implements OrderService {
  @Autowired private OrderRepository orderRepository;
  @Autowired private MerchantRepository merchantRepository;

  @Override
  public Order query(String accountId, String merchantTradeNumber, String merchantId) {
    // 判断查询权限
    if (getQueryPermission(accountId, merchantId)) {
      return orderRepository.findByMerchantTradeNumber(merchantId, merchantTradeNumber);
    }
    return null;
  }

  @Override
  public PageModel<Order> queryList(
      String accountId, String merchantId, Integer pageSize, Integer pageIndex) {
    if (getQueryPermission(accountId, merchantId)) {
      return orderRepository.findByMerchantId(merchantId, pageIndex, pageSize);
    }
    return null;
  }
  /**
   * 查询的权限校验
   *
   * @param accountId
   * @param merchantId
   * @return
   */
  private boolean getQueryPermission(String accountId, String merchantId) {
    Merchant merchant = merchantRepository.findById(merchantId);
    if (merchant == null) {
      return false;
    }
    if (!merchant.getAccountId().equals(accountId)) {
      return false;
    }
    if (merchant.getIsDelete()) {
      return false;
    }
    return true;
  }
}
