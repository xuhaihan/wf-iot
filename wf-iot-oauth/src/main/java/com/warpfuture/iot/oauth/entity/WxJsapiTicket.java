package com.warpfuture.iot.oauth.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "iot_wx_jsapi_ticket_info")
@Data
public class WxJsapiTicket {
  @Id private String appId; // 对应的应用id
  private String ticket; // 调用js微信接口的临时票据
  private Long expireTime; // 过期时间
}
