package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Transient;

/** Created by fido on 2018/4/20. 用于地图展示，包含设备Id,设备所在区域,设备Ip */
@Data
@ToString
public class DevInfo {
  private String deviceId; // 设备Id
  private String deviceIp; // 设备Ip
  @Transient private String deviceArea; // 设备所在区域
  private String latitude; // 纬度
  private String longitude; // 经度
}
