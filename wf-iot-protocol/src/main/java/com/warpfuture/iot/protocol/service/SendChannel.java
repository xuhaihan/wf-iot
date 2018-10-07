package com.warpfuture.iot.protocol.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/** Created by 徐海瀚 on 2018/4/18. */
public interface SendChannel {

  String OUTPUT1 = "p1";

  String OUTPUT2 = "p2";

  String OUTPUT3 = "p3";

  @Output(SendChannel.OUTPUT1)
  MessageChannel output1();

  @Output(SendChannel.OUTPUT2)
  MessageChannel output2();

  @Output(SendChannel.OUTPUT3)
  MessageChannel output3();
}
