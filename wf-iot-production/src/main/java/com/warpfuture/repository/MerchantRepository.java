package com.warpfuture.repository;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;

/** Created by fido on 2018/4/17. */
public interface MerchantRepository {

  public void create(Merchant merchant);

  public void delete(String merchantId);

  public void update(Merchant merchant);

  public Merchant findById(String merchantId);

  public PageModel<Merchant> findByAccountId(String accountId, Integer pageIndex, Integer pageSize);

  /**
   * 根据appid和mch_id查找微信相关的配置
   *
   * @param appId
   * @param wxPayMerchantId
   * @return
   */
  public Merchant findByAppIdAndMchId(String appId, String wxPayMerchantId);
}
