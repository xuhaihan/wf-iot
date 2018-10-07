package com.warpfuture.iot.protocol.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/** Created by 徐海瀚 on 2018/4/18. */

@Component
public interface ReceiveChannel {

  String INPUT1 = "c1";
  String INPUT2 = "c2";
  String INPUT3 = "c3";

  @Input(INPUT1)
  SubscribableChannel input1();

  @Input(INPUT2)
  SubscribableChannel input2();

  @Input(INPUT3)
  SubscribableChannel input3();
}
