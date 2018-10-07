package com.warpfuture.iot.protocol.entity;

/** Created by 徐海瀚 on 2018/4/13. 调用接口应答格式类 */
public class RepayEntity<T> {
  private int code;
  private String message;
  private T data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
