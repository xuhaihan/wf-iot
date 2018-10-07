package com.warpfuture.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** @Auther: fido @Date: 2018/6/12 00:06 @Description:将设备上报数据回调给业务方 */
@Data
@ToString
@Document(collection = "iot_notify_message")
public class NotifyMsg {
  @Id private String accountId; // 账号Id
  private String msgNotifyURL; // 业务方的回调地址
  private Boolean isAccept; // 是否接受回调消息
}
