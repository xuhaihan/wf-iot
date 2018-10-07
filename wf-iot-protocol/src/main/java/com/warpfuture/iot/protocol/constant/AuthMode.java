package com.warpfuture.iot.protocol.constant;

/** Created by 徐海瀚 on 2018/4/13. 设备认证登陆认证方式枚举类 */
public enum AuthMode {
  N("N"),
  ECC("ECC"),
  RSA("RSA");
  private String desc;

  private AuthMode(String desc) {
    this.desc = desc;
  }

  public static AuthMode getByValue(String value) {
    for (AuthMode authMode : values()) {
      if (authMode.getDesc().equals(value)) {
        return authMode;
      }
    }
    return null;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
