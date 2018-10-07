package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;

/** @Auther: fido @Date: 2018/6/15 11:09 @Description:用于接收设备登录信息 */
@ToString
@Data
public class DeviceLoginDto {
  private String productionKey;
  private String deviceId;
  // 以上两个是弱校验使用

  // 以下两个是强校验使用
  private String pksToken;
  private String mode;
}
