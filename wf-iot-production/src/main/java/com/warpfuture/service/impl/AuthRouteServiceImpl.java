package com.warpfuture.service.impl;

import com.warpfuture.entity.RouteInfo;
import com.warpfuture.repository.AuthRouteRepository;
import com.warpfuture.service.AuthRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** @Auther: fido @Date: 2018/6/28 18:02 @Description: */
@Service
public class AuthRouteServiceImpl implements AuthRouteService {
  @Autowired private AuthRouteRepository authRouteRepository;

  @Override
  public List<RouteInfo> findDeviceAuthRecord(
      String accountId, String productionId, String deviceId) {
    return authRouteRepository.findDeviceAuthRecord(accountId, productionId, deviceId);
  }
}
