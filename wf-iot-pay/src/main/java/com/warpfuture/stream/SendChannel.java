package com.warpfuture.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface SendChannel {

  String OUTPUT = "output";

  @Output(SendChannel.OUTPUT)
  MessageChannel output();
}
