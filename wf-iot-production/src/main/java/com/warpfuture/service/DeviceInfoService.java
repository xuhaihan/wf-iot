package com.warpfuture.service;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;

/** Created by fido on 2018/4/23. */
public interface DeviceInfoService {

  public PageModel<Device> getOnlineDevsByProd(
      String accountId, String productionId, Integer pageSize, Integer pageIndex);

  public PageModel<Device> getOffLineDevsByProd(
      String accountId, String productionId, Integer pageSize, Integer pageIndex);

  public PageModel<Device> getAllDevsByProd(
      String accountId, String productionId, Integer pageSize, Integer pageIndex);

  public PageModel<Device> getOnlineDevsByAccount(
      String accountId, Integer pageSize, Integer pageIndex);

  public PageModel<Device> getOffLineDevsByAccount(
      String accountId, Integer pageSize, Integer pageIndex);

  public PageModel<Device> getAllDevsByAccount(
      String accountId, Integer pageSize, Integer pageIndex);

  public Device getDeviceByDevId(String accountId, String productionId, String deviceCloudId);
}
