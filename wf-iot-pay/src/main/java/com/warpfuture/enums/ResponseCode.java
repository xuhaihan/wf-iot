package com.warpfuture.enums;

/** Created by fido on 2018/4/13. 状态码枚举 */
public enum ResponseCode {
  MERCHANT_OPERATION_SUCCESS(1, "商户操作成功"),
  MERCHANT_OPERATION_FAIL(0, "商户操作失败"),
  WXPAY_OPERATION_SUCCESS(1, "操作成功"),
  WXPAY_OPERATION_FAIL(0, "操作失败"),
  ORDER_QUERY_SUCCESS(1, "查询订单操作成功"),
  ORDER_QUERY_FAIL(0, "查询订单操作失败");
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
