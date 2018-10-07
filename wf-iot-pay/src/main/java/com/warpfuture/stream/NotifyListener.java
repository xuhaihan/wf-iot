package com.warpfuture.stream;

import org.springframework.messaging.handler.annotation.Payload;

/** Created by fido on 2018/5/16. */
public interface NotifyListener {
  public void receiveNotifyMsg(@Payload String payload);
}
