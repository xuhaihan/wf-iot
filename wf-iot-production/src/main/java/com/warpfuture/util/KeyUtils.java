package com.warpfuture.util;

import java.util.UUID;

/** Created by fido on 2018/4/13. 生成产品密码 */
public class KeyUtils {

  public static String createProductionKey() {
    return UUID.randomUUID().toString().replaceAll("-", ""); // 生成唯一的产品密码
  }
}
