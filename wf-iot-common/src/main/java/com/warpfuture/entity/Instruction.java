package com.warpfuture.entity;

import java.util.Map;

/**
 * 徐海瀚
 *
 * <p>主要给企业对设备/用户使用的指令
 */
public class Instruction {
  private String statement; // 说明什么指令操作
  private String messageTime; // 下发指令的时间
  private Map<String, Object> data; // 相关指令需要的数据

  public String getStatement() {
    return statement;
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }

  public String getMessageTime() {
    return messageTime;
  }

  public void setMessageTime(String messageTime) {
    this.messageTime = messageTime;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}
