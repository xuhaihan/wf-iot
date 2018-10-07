package com.warpfuture.service.impl;

import com.warpfuture.dto.DeviceData;
import com.warpfuture.entity.Device;
import com.warpfuture.repository.DeviceRepository;
import com.warpfuture.service.DeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Created by fido on 2018/5/6. */
@Service
public class DeviceDataServiceImpl implements DeviceDataService {
  @Autowired private DeviceRepository deviceRepository;

  @Override
  public DeviceData getDeviceData(String accountId, Long todayTime) {
    long deviceCount = deviceRepository.getDeviceCount(accountId); // 设备总量
    long onlineCount = deviceRepository.getTodayOnlineNums(accountId, todayTime); // 今日设备在线量
    long activeCount = deviceRepository.getTodayActive(accountId, todayTime); // 今日设备激活量
    long sevenOnlineCount =
        deviceRepository.getSevenDaysActiveNums(accountId, todayTime); // 近七日设备在线量
    DeviceData deviceData = new DeviceData();
    deviceData.setAccountId(accountId);
    deviceData.setActivateNums(deviceCount);
    deviceData.setCurrentOnlineNums(onlineCount);
    deviceData.setCurrentActivateNums(activeCount);
    deviceData.setSevenDaysOnlineNums(sevenOnlineCount);
    return deviceData;
  }

  @Override
  public Device updateExtensions(Device device) {
    return deviceRepository.updateExtensions(device);
  }
}
