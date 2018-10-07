package com.warpfuture.service.impl;

import com.warpfuture.constant.RedisConstant;
import com.warpfuture.constant.RelationConstant;
import com.warpfuture.entity.ReportUser;
import com.warpfuture.entity.Route;
import com.warpfuture.entity.RouteInfo;
import com.warpfuture.repository.ReportUserRepository;
import com.warpfuture.repository.RouteInfoRepository;
import com.warpfuture.repository.RouteRepository;
import com.warpfuture.service.RedisService;
import com.warpfuture.service.RouteService;
import com.warpfuture.util.VertifyRouteUtils;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/** Created by fido on 2018/4/14. */
@Service
public class RouteServiceImpl implements RouteService {

  private Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class);

  @Autowired private RedisService redisService;

  @Autowired private RouteRepository routeRepository;

  @Autowired private RouteInfoRepository routeInfoRepository;

  @Autowired private ReportUserRepository reportUserRepository;

  @Override
  public List<String> devToUser(String productionId, String deviceId) {
    long start = System.currentTimeMillis();
    logger.info("--设备鉴权接收到的数据：--" + "productionId:" + productionId + "deviceId:" + deviceId);
    String keyPattern =
        RedisConstant.REPORT_KEY + "*" + productionId + "*" + deviceId; // 关于这个设备的所有用户列表
    logger.info("--设备鉴权时key pattern为:---" + keyPattern);
    // 获得用户列表
    Set<String> userKeySet = redisService.queryByPattern(keyPattern); // 只是模糊得到key列表
    List<String> userList = null;
    // 根据key找value
    if (userKeySet != null && userKeySet.size() > 0) {
      Set<String> userValueSet = new HashSet<>();
      for (String userKey : userKeySet) {
        userValueSet.add(redisService.getValue(userKey));
      }
      userList = new LinkedList<String>();
      // 对正在使用设备的用户进行上报数据的鉴权
      for (String userId : userValueSet) {
        userList.add(userId);
      }
      long endFindList = System.currentTimeMillis();
      logger.info("===设备鉴权时校验通信方向用时==" + (endFindList - start) + "ms");
    }
    // redis找不到记录
    else {
      List<RouteInfo> list = routeInfoRepository.findByDevice(productionId, deviceId);
      if (list != null && list.size() > 0) {
        userList = new LinkedList<>();
        for (RouteInfo routeInfo : list) {
          // 可以使用
          if (VertifyRouteUtils.ifLegal(routeInfo, System.currentTimeMillis())) {
            userList.add(routeInfo.getUserId());
            String usingKey =
                RedisConstant.USING_KEY
                    + routeInfo.getApplicationId()
                    + ":"
                    + routeInfo.getUserId()
                    + ":"
                    + routeInfo.getProductionId()
                    + ":"
                    + routeInfo.getDeviceId();
            String usingValue = routeInfo.getDeviceId();
            redisService.setKeyAndValueWithTime(
                usingKey,
                usingValue,
                routeInfo.getEndTime() - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS);
            if (routeInfo.getReport()) {
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
              redisService.setKeyAndValueWithTime(
                  reportKey,
                  reportValue,
                  routeInfo.getEndTime() - System.currentTimeMillis(),
                  TimeUnit.MILLISECONDS);
            }
          }
        }
      }
      // 没有任何使用记录,需要利用广播模式
      else {
        logger.info("==进入广播模式==");
        List<Route> broadcastList = routeRepository.findByProAndBroadcast(productionId);
        logger.info("==符合广播模式的路由记录" + broadcastList);
        userList = new LinkedList<>();
        for (Route route : broadcastList) {
          List<ReportUser> reportUsers =
              reportUserRepository.getUserIdList(route.getApplicationId());
          for (ReportUser reportUser : reportUsers) {
            userList.add(reportUser.getUserId());
          }
        }
        logger.info("==查找广播的用户列表==" + userList);
      }
    }
    logger.info("--设备鉴权时返回的用户列表--：" + userList);
    long end = System.currentTimeMillis();
    logger.info("--设备鉴权获取用户列表程序用时：--" + (end - start) + "ms");
    return userList;
  }

  @Override
  public boolean userToDev(
      String applicationId, String productionId, String deviceId, String userId) {
    long start = System.currentTimeMillis();
    String usingKey =
        RedisConstant.USING_KEY
            + applicationId
            + ":"
            + userId
            + ":"
            + productionId
            + ":"
            + deviceId;
    logger.info("---查询用户的请求连接结果的redis key ：--" + usingKey);
    String device = redisService.getValue(usingKey);
    if (device != null) {
      return true;
    }
    // redis不存在，去MongoDB查
    else {
      logger.info("==redis中查询不到使用记录===");
      RouteInfo routeInfo =
          routeInfoRepository.findByUser(productionId, applicationId, deviceId, userId);
      if (routeInfo == null) {
        return false;
      } else {
        if (VertifyRouteUtils.ifLegal(routeInfo, System.currentTimeMillis())) {
          String key =
              RedisConstant.USING_KEY
                  + routeInfo.getApplicationId()
                  + ":"
                  + routeInfo.getUserId()
                  + ":"
                  + routeInfo.getProductionId()
                  + ":"
                  + routeInfo.getDeviceId();
          String usingValue = routeInfo.getDeviceId();
          redisService.setKeyAndValueWithTime(
              usingKey, usingValue, routeInfo.getEndTime() - System.currentTimeMillis());
          return true;
        }
      }
    }
    return false;
  }
  //
  //  /**
  //   * 根据前面缓存的userId-applicationId关系 找到applicationId，并找到对应的权限关系
  //   *
  //   * @param userId
  //   * @param productionId
  //   * @return
  //   */
  //  private Route getRoute(String userId, String productionId) {
  //    String key = RedisConstant.RELATION_KEY + userId;
  //    String applicationId = redisService.getValue(key);
  //    Route route = routeRepository.findByTwoId(productionId, applicationId);
  //    return route;
  //  }
  //
  //  private boolean ifReport(Route route) {
  //    if (route == null) {
  //      return false;
  //    }
  //    // 存在上报或者双向通信的权限
  //    if (route.getRelation() == RelationConstant.RELATION_REPORT
  //        || route.getRelation() == RelationConstant.RELATION_TWOWAY) {
  //      return true;
  //    }
  //    return false;
  //  }
  //
  //  // 是否允许广播
  //  private boolean ifBroadcast(Route route) {
  //    if (route == null) {
  //      return false;
  //    }
  //    return route.getBroadcast();
  //  }
}
