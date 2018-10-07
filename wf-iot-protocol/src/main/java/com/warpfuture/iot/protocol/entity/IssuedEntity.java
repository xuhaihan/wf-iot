package com.warpfuture.iot.protocol.entity;

import java.util.Map;

/** Created by 徐海瀚 on 2018/4/13. 下发消息类 */
public class IssuedEntity {
  private String accountId;
  private String data;
  private Long messageTime;
  private Map<String, Object> source;
  private Map<String, Object> target;

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Long getMessageTime() {
    return messageTime;
  }

  public void setMessageTime(Long messageTime) {
    this.messageTime = messageTime;
  }

  public Map<String, Object> getSource() {

    return source;
  }

  public void setSource(Map<String, Object> source) {
    this.source = source;
  }

  public Map<String, Object> getTarget() {
    return target;
  }

  public void setTarget(Map<String, Object> target) {
    this.target = target;
  }
}
