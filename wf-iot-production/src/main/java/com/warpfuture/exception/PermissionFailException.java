package com.warpfuture.exception;

/**
 * Created by fido on 2018/4/19.
 */
public class PermissionFailException extends RuntimeException {
    public PermissionFailException(String message) {
        super(message);
  }
}
