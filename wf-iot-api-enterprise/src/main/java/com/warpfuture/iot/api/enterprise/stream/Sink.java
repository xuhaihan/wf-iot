package com.warpfuture.iot.api.enterprise.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    String INPUT = "input";

    @Input(value = Sink.INPUT)
    SubscribableChannel receive();
}
