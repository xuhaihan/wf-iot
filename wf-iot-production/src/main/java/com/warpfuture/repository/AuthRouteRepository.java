package com.warpfuture.repository;


import com.warpfuture.entity.RouteInfo;

import java.util.List;

/** @Auther: fido @Date: 2018/6/28 17:46 @Description: */
public interface AuthRouteRepository {
  public List<RouteInfo> findDeviceAuthRecord(
      String accountId, String productionId, String deviceId);
}
