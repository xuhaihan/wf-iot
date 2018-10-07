package com.warpfuture.util;

import com.warpfuture.entity.merchant.Merchant;

/** Created by fido on 2018/5/16. */
public class PermissionVertifyUtils {
  /**
   * 操作的权限校验
   *
   * @param accountId
   * @param merchant
   * @return
   */
  public static boolean getPayPermission(String accountId, Merchant merchant) {
    // 已被删除
    if (merchant.getIsDelete() == true) {
      return false;
    }
    // 对应商户信息的账户Id与给定的账户Id是否相同
    if (!merchant.getAccountId().equals(accountId)) {
      return false;
    }
    // 没有配置微信支付相关的信息
    if (merchant.getWxPayData() == null) {
      return false;
    }
    return true;
  }
}
