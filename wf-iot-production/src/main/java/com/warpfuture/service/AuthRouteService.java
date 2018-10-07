package com.warpfuture.service;

import com.warpfuture.entity.RouteInfo;

import java.util.List;

/**
 * @Auther: fido
 * @Date: 2018/6/28 18:01
 * @Description:
 */
public interface AuthRouteService {
  public List<RouteInfo> findDeviceAuthRecord(
      String accountId, String productionId, String deviceId);
}
