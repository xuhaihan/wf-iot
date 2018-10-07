package com.warpfuture.entity;

import com.warpfuture.constant.ResponseCode;
import lombok.ToString;

/** Created by 徐海瀚 on 2018/4/13. 调用接口应答格式类 */
@ToString
public class RepayEntity<T> {
  private int code;
  private String message;
  private T data;

  public RepayEntity fail(String message) {
    this.code = ResponseCode.FAIL;
    this.message = message;
    return this;
  }

  public RepayEntity(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public RepayEntity(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public RepayEntity() {}

  public boolean isTokenSuccess() {
    return this.code == ResponseCode.TOKEN_RIGHT;
  }

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
