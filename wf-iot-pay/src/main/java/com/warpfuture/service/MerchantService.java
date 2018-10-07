package com.warpfuture.service;

import com.warpfuture.entity.merchant.Merchant;

import java.util.List;

/** Created by fido on 2018/4/17. */
public interface MerchantService {

  public Merchant findById(String accountId, String merchantId);
}
