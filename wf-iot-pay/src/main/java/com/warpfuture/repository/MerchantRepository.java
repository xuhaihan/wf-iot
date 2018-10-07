package com.warpfuture.repository;

import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.merchant.WxpayData;

/** Created by fido on 2018/4/17. */
public interface MerchantRepository {

  public Merchant findById(String merchantId);

  public WxpayData getByMerchantId(String merchantId);

  /**
   * 根据appid和mch_id查找微信相关的配置
   *
   * @param appId
   * @param wxPayMerchantId
   * @return
   */
  public Merchant findByAppIdAndMchId(String appId, String wxPayMerchantId);
}
