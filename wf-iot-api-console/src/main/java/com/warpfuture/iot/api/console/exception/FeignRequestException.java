package com.warpfuture.iot.api.console.exception;

public class FeignRequestException extends RuntimeException {
    public FeignRequestException() {
    }

    public FeignRequestException(String message) {
        super(message);
    }
}
