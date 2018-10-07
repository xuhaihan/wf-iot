package com.warpfuture.util;

import com.warpfuture.entity.Production;

/** Created by fido on 2018/4/23. 判断权限 */
public class PermissionUtils {

  public static boolean permission(Production production, String accountId) {
    if (production == null) {
      return false;
    }
    return production.getAccountId().equals(accountId);
  }
}
