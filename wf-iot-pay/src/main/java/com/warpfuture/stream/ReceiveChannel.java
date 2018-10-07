package com.warpfuture.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface ReceiveChannel {

  String INPUT = "input";

  @Input(ReceiveChannel.INPUT)
  SubscribableChannel input();
}
