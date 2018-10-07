package com.warpfuture.service.impl;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.DeviceInfoRepository;
import com.warpfuture.repository.ProductionRepository;
import com.warpfuture.service.DeviceInfoService;
import com.warpfuture.util.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by fido on 2018/4/23. */
@Service
public class DeviceInfoServiceImpl implements DeviceInfoService {
  @Autowired private DeviceInfoRepository deviceInfoRepository;
  @Autowired private ProductionRepository productionRepository;

  @Override
  public PageModel<Device> getOnlineDevsByProd(
      String accountId, String productionId, Integer pageSize, Integer pageIndex) {
    Production production = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(production, accountId)) {
      throw new PermissionFailException("查询产品下在线设备权限错误");
    }
    return deviceInfoRepository.getOnlineDevicesByProd(productionId, pageIndex, pageSize);
  }

  @Override
  public PageModel<Device> getOffLineDevsByProd(
      String accountId, String productionId, Integer pageSize, Integer pageIndex) {
    Production production = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(production, accountId)) {
      throw new PermissionFailException("查询产品下离线设备权限错误");
    }
    return deviceInfoRepository.getOffLineDevicesByProd(productionId, pageIndex, pageSize);
  }

  @Override
  public PageModel<Device> getAllDevsByProd(
      String accountId, String productionId, Integer pageSize, Integer pageIndex) {
    Production production = productionRepository.findByProductionId(productionId);
    if (!PermissionUtils.permission(production, accountId)) {
      throw new PermissionFailException("查询产品下所有设备权限错误");
    }
    return deviceInfoRepository.getAllDevicesByProd(productionId, pageIndex, pageSize);
  }

  @Override
  public PageModel<Device> getOnlineDevsByAccount(
      String accountId, Integer pageSize, Integer pageIndex) {
    return deviceInfoRepository.getOnlineDevicesByAccount(accountId, pageIndex, pageSize);
  }

  @Override
  public PageModel<Device> getOffLineDevsByAccount(
      String accountId, Integer pageSize, Integer pageIndex) {
    return deviceInfoRepository.getOffLineDeviceByAccount(accountId, pageIndex, pageSize);
  }

  @Override
  public PageModel<Device> getAllDevsByAccount(
      String accountId, Integer pageSize, Integer pageIndex) {
    return deviceInfoRepository.getAllDevicesByAccount(accountId, pageIndex, pageSize);
  }

  @Override
  public Device getDeviceByDevId(String accountId, String productionId, String deviceId) {
    Device device = deviceInfoRepository.findByDeviceId(accountId, productionId, deviceId);
    if (device == null) {
      throw new PermissionFailException("此设备不存在");
    }
    return device;
  }
}
