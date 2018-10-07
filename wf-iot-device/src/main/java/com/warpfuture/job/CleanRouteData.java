package com.warpfuture.job;

import com.warpfuture.entity.RouteInfo;
import com.warpfuture.repository.RouteInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/** Created by fido on 2018/5/12. */
@Component
public class CleanRouteData {
  private Logger logger = LoggerFactory.getLogger(CleanRouteData.class);
  @Autowired public RouteInfoRepository routeInfoRepository;

  // 每隔两个小时清除一次过期记录
  @Scheduled(fixedDelay = 3600 * 2 * 1000)
  public void cleanRouteData() {
    logger.info("==开始查找过期的路由记录==");
    // 找到所有过期的权限
    List<RouteInfo> routeInfoList =
        routeInfoRepository.findOutOfDateRoute(System.currentTimeMillis());
    // 清除过期的权限
    for (RouteInfo routeInfo : routeInfoList) {
      logger.info("==清除过期鉴权权限==" + routeInfo);
      routeInfoRepository.delete(routeInfo);
    }
  }
}
