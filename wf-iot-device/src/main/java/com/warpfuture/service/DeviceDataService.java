package com.warpfuture.service;

import com.warpfuture.dto.DeviceData;
import com.warpfuture.entity.Device;

/** Created by fido on 2018/5/6. 数据统计，统计账户下的设备总量，今日设备激活量，今日设备在线量，近七日设备活跃量 */
public interface DeviceDataService {
  public DeviceData getDeviceData(String accountId, Long todayTime);

  public Device updateExtensions(Device device);
}
