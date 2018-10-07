package com.warpfuture.job;

import com.warpfuture.entity.RouteInfo;
import com.warpfuture.repository.RouteInfoRepository;
import com.warpfuture.service.Write2RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Created by fido on 2018/5/12. 将mongodb中有的鉴权数据加载到redis里面 */
@Component
public class GetRouteData implements CommandLineRunner {
  @Autowired private RouteInfoRepository routeInfoRepository;
  @Autowired private Write2RedisService write2RedisService;
  private Logger logger = LoggerFactory.getLogger(GetRouteData.class);

  @Override
  public void run(String... strings) throws Exception {
    List<RouteInfo> routeInfoList = routeInfoRepository.getAllRoute(); // 获取所有鉴权数据
    for (RouteInfo routeInfo : routeInfoList) {
      logger.info("==从MongoDB中载入数据到redis==：" + routeInfo);
      String applicationId = routeInfo.getApplicationId();
      String productionId = routeInfo.getProductionId();
      String userId = routeInfo.getUserId();
      String deviceId = routeInfo.getDeviceId();
      Long keepTime = routeInfo.getKeepTime();
      Long startTime = routeInfo.getStartTime();
      Long endTime = routeInfo.getEndTime();
      try {
        // 还未到时间
        if (startTime > System.currentTimeMillis()) {
          continue;
        }
        if (endTime < System.currentTimeMillis()) {
          continue;
        }
        write2RedisService.writeRoute2Redis(routeInfo);
        logger.info("==需要放入的路由记录==" + routeInfo);
      } catch (Exception e) {
        logger.info("==载入数据有误==");
      }
    }
  }
}
