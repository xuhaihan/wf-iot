package com.warpfuture.enums;

/** Created by fido on 2018/4/13. 状态码枚举 */
public enum ResponseCode {
  LOGIN_AUTH_SUCCESS(4001, "设备登录认证成功"),
  LOGIN_AUTH_FAIL(4002, "设备登录认证失败"),
  REQUEST_CONNECT_SUCCESS(4003, "配置用户与设备权限成功"),
  REQUEST_CONNECT_FAIL(4004, "配置用户与设备权限失败"),
  REMOVE_CONNECT_SUCCESS(4005, "删除用户与设备权限成功"),
  REMOVE_CONNECT_FAIL(4006, "删除用户与设备权限失败"),
  ROUTE_USERTODEV_SUCCESS(4007, "用户下发指令鉴权成功"),
  ROUTE_USERTODEV_FAIL(4008, "用户下发指令鉴权成功"),
  ROUTE_DEVTOUSER_SUCCESS(4009, "设备上报数据鉴权成功"),
  ROUTE_DEVTOUSER_FAIL(4010, "设备上报数据鉴权失败"),
  QUERY_SUCCESS(1, "查询成功"),
  QUERY_FAIL(0, "查询失败"),
  OPERATION_SUCCESS(1, "操作成功"),
  OPERATION_FAIL(0, "操作失败");

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
