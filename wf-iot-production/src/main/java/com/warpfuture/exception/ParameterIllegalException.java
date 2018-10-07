package com.warpfuture.exception;

/**
 * Created by fido on 2018/4/23. 参数非法
 */
public class ParameterIllegalException extends RuntimeException {
    public ParameterIllegalException(String message) {
        super(message);
  }
}
