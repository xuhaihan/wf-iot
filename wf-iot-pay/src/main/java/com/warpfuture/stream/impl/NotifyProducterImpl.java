package com.warpfuture.stream.impl;

import com.warpfuture.stream.NotifyProducter;
import com.warpfuture.stream.SendChannel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

/** Created by fido on 2018/5/16. */
@EnableBinding(value = {SendChannel.class})
@Service
@Log4j2
public class NotifyProducterImpl implements NotifyProducter {
  @Autowired private SendChannel sendChannel;

  @Override
  public void sendNotifyMsg(String message) {
    if (message != null) {
      try {
        log.info("==kafka开始发送消息===");
        sendChannel.output().send(MessageBuilder.withPayload(message).build());
        log.info("==kafka发送消息完毕==");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
