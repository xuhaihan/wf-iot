package com.warpfuture.iot.protocol.service.impl.middleware;

import com.warpfuture.iot.protocol.service.ProducerService;
import com.warpfuture.iot.protocol.service.SendChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

/** Created by 徐海瀚 on 2018/4/18. */
@EnableBinding(value = {SendChannel.class})
@Service
public class ProducerServiceImpl implements ProducerService {

  @Autowired private SendChannel sendChannel;

  public void sendDataMsg(String message) {
    if (message != null) {
      try {
        sendChannel.output1().send(MessageBuilder.withPayload(message).build());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void sendLoginMsg(String message) {
    if (message != null) sendChannel.output2().send(MessageBuilder.withPayload(message).build());
  }

  public void sendOutlineMsg(String message) {
    if (message != null) sendChannel.output3().send(MessageBuilder.withPayload(message).build());
  }
}
