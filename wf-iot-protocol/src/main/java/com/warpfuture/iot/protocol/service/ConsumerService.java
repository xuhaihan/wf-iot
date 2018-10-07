package com.warpfuture.iot.protocol.service;

import org.springframework.messaging.handler.annotation.Payload;


/** Created by 徐海瀚 on 2018/4/23. */
public interface ConsumerService {
  public void receiveDataMsg(@Payload String payload);

  public void receiveLoginMsg(@Payload String payload);

  public void receiveOutlineMsg(@Payload String payload);
}
