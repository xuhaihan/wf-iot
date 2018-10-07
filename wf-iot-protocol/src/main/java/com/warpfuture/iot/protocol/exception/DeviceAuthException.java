package com.warpfuture.iot.protocol.exception;

/** Created by 徐海瀚 on 2018/4/13. */
public class DeviceAuthException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private String errorCode;

  public DeviceAuthException(String message) {
    super(message);
  }

  public DeviceAuthException(String errorCode, String message, Throwable cause) {
    super(message, cause);
    this.setErrorCode(errorCode);
  }

  public DeviceAuthException(String message, Throwable cause) {
    super(message, cause);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
