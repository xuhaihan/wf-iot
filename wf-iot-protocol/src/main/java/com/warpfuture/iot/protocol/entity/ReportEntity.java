package com.warpfuture.iot.protocol.entity;

import java.util.Map;

/** Created by 徐海瀚 on 2018/4/13. 上报格式类 */
public class ReportEntity {
  private String accountId;
  private Map<String, Object> source;
  private Long messageTime;
  private String data;

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public Object getSource() {
    return source;
  }

  public void setSource(Map<String, Object> source) {
    this.source = source;
  }

  public Long getMessageTime() {
    return messageTime;
  }

  public void setMessageTime(Long messageTime) {
    this.messageTime = messageTime;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
