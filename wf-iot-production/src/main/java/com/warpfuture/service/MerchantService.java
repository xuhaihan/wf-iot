package com.warpfuture.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;

import java.util.List;

/** Created by fido on 2018/4/17. */
public interface MerchantService {

  public Merchant create(Merchant merchant);

  public Merchant update(Merchant merchant);

  public void delete(String accountId, String merchantId);

  public Merchant findById(String accountId, String merchantId);

  public PageModel<Merchant> findByAccountId(String accountId, Integer pageIndex, Integer pageSize);
}
