package com.warpfuture.iot.oauth.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "iot_wx_ordinaryAccessToken_info")
@Data
public class OrdinaryAccessToken {
  @Id private String appId; // 对应某个应用的appid
  private String access_token; // 获取微信的普通access_token，对应某个应用的access_token
  private Long expireTime; // 过期时间
}
