package com.warpfuture.constant;

/** Created by fido on 2018/4/19. 产品相关的常量 */
public class Constant {
  public static final Integer ENCRYPT_NULL = 0; // 不加密
  public static final Integer ENCRYPT_ECC = 1; // ECC
  public static final Integer ENCRYPT_RSA = 2; // RSA
  public static final Integer STATUS_READY = 0; // 未发布
  public static final Integer STATUS_PUBLISH = 1; // 发布
  public static final Integer STATUS_REVOKE = 2; // 下架
  public static final Integer SUCCESS = 1; // 成功
  public static final Integer FAIL = 0; // 失败
  public static final Integer OTA_ALIVE = 0; // ota状态 激活
  public static final Integer OTA_DISABLED = 1; // ota状态 禁用
  public static final Integer OTA_NEEDTOUPDATE = 5001; // 设备需要升级
  public static final Integer OTA_NONEDDTOUPDATE = 5002; // 设备无需升级
}
