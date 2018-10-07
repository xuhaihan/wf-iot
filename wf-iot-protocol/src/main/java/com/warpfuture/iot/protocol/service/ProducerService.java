package com.warpfuture.iot.protocol.service;

/** Created by 徐海瀚 on 2018/4/23. */
public interface ProducerService {
  public void sendDataMsg(String message);

  public void sendLoginMsg(String message);

  public void sendOutlineMsg(String message);
}
