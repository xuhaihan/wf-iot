package com.warpfuture.service.impl;

import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.MerchantRepository;
import com.warpfuture.service.MerchantService;
import com.warpfuture.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Created by fido on 2018/4/17. */
@Service
public class MerchantServiceImpl implements MerchantService {

  @Autowired private MerchantRepository merchantRepository;

  @Override
  public Merchant findById(String accountId, String merchantId) {
    if (!ifHasPower(accountId, merchantId)) {
      throw new PermissionFailException("查询商户权限不足");
    }
    return merchantRepository.findById(merchantId);
  }

  /**
   * 是否有权限操作
   *
   * @param accountId
   * @param merchantId
   * @return
   */
  private boolean ifHasPower(String accountId, String merchantId) {
    Merchant originMerchant = merchantRepository.findById(merchantId);
    if (originMerchant == null) {
      return false;
    }
    if (!originMerchant.getAccountId().equals(accountId)) {
      return false;
    } else {
      return true;
    }
  }
}
