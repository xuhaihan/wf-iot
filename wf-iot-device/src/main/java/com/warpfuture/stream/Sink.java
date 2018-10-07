package com.warpfuture.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/** Created by fido on 2018/4/19. */
public interface Sink {
  String INPUT = "input"; // 定义输入通道
  @Input(INPUT)
  SubscribableChannel inboundData();
}
