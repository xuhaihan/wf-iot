package com.warpfuture.service.impl;

import com.warpfuture.constant.RedisConstant;
import com.warpfuture.entity.RouteInfo;
import com.warpfuture.service.RedisService;
import com.warpfuture.service.Write2RedisService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @Auther: fido @Date: 2018/5/19 18:27 @Description: */
@Service
@Log4j2
public class Write2RedisServiceImpl implements Write2RedisService {
  @Autowired private RedisService redisService;

  @Override
  public void writeRoute2Redis(RouteInfo routeInfo) {
    log.info("需要放入redis里的路由记录为=" + routeInfo);
    Long keepTime = routeInfo.getKeepTime();
    String usingKey =
        RedisConstant.USING_KEY
            + routeInfo.getApplicationId()
            + ":"
            + routeInfo.getUserId()
            + ":"
            + routeInfo.getProductionId()
            + ":"
            + routeInfo.getDeviceId();
    String reportKey =
        RedisConstant.REPORT_KEY
            + routeInfo.getApplicationId()
            + ":"
            + routeInfo.getUserId()
            + ":"
            + routeInfo.getProductionId()
            + ":"
            + routeInfo.getDeviceId();
    String reportValue = routeInfo.getUserId();
    String usingValue = routeInfo.getDeviceId();
    if (keepTime != -1) {
      redisService.setKeyAndValueWithTime(usingKey, usingValue, keepTime);
      if (routeInfo.getReport()) {
        redisService.setKeyAndValueWithTime(reportKey, reportValue, keepTime);
      }
    } else {
      redisService.setKeyAndValue(usingKey, usingValue);
      if (routeInfo.getReport()) {
        redisService.setKeyAndValue(reportKey, reportValue);
      }
    }
  }
}
