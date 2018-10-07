package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/** @Auther: fido @Date: 2018/5/23 20:07 @Description: 设备上报信息用于ota固件升级 */
@ToString
@Data
public class UploadDeviceInfo {
  private String originOtaVersion; //设备当前的版本
  private String deviceId;//设备id
  private String productionKey;//产品密钥
  private Map<String, Object> extensions; //拓展字段
}
