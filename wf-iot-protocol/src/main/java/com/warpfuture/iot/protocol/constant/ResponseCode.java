package com.warpfuture.iot.protocol.constant;

/** Created by 徐海瀚 on 2018/4/13. API状态码枚举类 */
public enum ResponseCode {
  USER_TOKEN_SUCCESS(2000, "用户token验证成功"),
  USER_TOKEN_FAIL(2001, "用户token验证失败"),
  LOGIN_AUTH_SUCCESS(4001, "设备登录认证成功"),
  LOGIN_AUTH_FAIL(4002, "设备登录认证失败"),
  ROUTE_USERTODEV_SUCCESS(4007, "用户下发指令鉴权成功"),
  ROUTE_USERTODEV_FAIL(4008, "用户下发指令鉴权成功"),
  ROUTE_DEVTOUSER_SUCCESS(4009, "设备上报数据鉴权成功"),
  ROUTE_DEVTOUSER_FAIL(4010, "设备上报数据鉴权失败");

  private final int code; // 状态码
  private final String description; // 描述

  ResponseCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
}
