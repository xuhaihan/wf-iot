package com.warpfuture.iot.protocol.entity;

/** Created by 徐海瀚 on 2018/4/13. 非上报下发的记录消息 */
public class RecordEntity<T> {
  private String message;
  private Long messageTime;
  private T data;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getMessageTime() {
    return messageTime;
  }

  public void setMessageTime(Long messageTime) {
    this.messageTime = messageTime;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
