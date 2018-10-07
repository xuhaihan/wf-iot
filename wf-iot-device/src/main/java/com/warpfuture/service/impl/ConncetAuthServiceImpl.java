package com.warpfuture.service.impl;

import com.warpfuture.constant.RedisConstant;
import com.warpfuture.constant.RelationConstant;
import com.warpfuture.entity.*;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.*;
import com.warpfuture.service.ConnectAuthService;
import com.warpfuture.service.RedisService;
import com.warpfuture.service.Write2RedisService;
import com.warpfuture.util.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/** Created by fido on 2018/4/14. */
@Service
public class ConncetAuthServiceImpl implements ConnectAuthService {

  @Autowired private RedisService redisService;
  @Autowired private RouteRepository routeRepository;
  @Autowired private FindProductInfoRepository productionRepository;
  @Autowired private ApplicationRepository applicationRepository;
  @Autowired private DeviceRepository deviceRepository;
  @Autowired private RouteInfoRepository routeInfoRepository;
  @Autowired private Write2RedisService write2RedisService;
  private Logger logger = LoggerFactory.getLogger(ConncetAuthServiceImpl.class);

  @Override
  public boolean addConnectAuth(
      String accountId,
      String productionId,
      String deviceId,
      String userId,
      String applicationId,
      Long keepTime,
      Long startTime) {
    long start = System.currentTimeMillis();
    // 判断应用，产品与账户之间的所属权
    if (!getPermisson(accountId, productionId, applicationId)) {
      throw new PermissionFailException("权限错误");
    }
    // 判断设备所属权
    if (!getDevPermission(accountId, productionId, deviceId)) {
      throw new PermissionFailException("权限错误");
    }
    // 判断应用与产品之间是否有权限
    Route route = routeRepository.findByTwoId(productionId, applicationId);
    boolean result = false;
    // 应用与产品之间没有权限
    if (route == null) {
      throw new PermissionFailException("权限错误");
    }
    // 只允许设备上报信息，不允许用户控制
    if (route.getRelation() == RelationConstant.RELATION_REPORT) {
      throw new PermissionFailException("路由配置仅允许设备上报");
    }
    RouteInfo routeInfo = new RouteInfo();
    routeInfo.setAccountId(accountId);
    routeInfo.setApplicationId(applicationId);
    routeInfo.setDeviceId(deviceId);
    routeInfo.setUserId(userId);
    routeInfo.setProductionId(productionId);
    boolean report = route.getRelation() == RelationConstant.RELATION_ISSUED ? false : true;
    routeInfo.setReport(report);
    if (startTime != null) {
      routeInfo.setStartTime(startTime);
    } else {
      startTime = System.currentTimeMillis();
      routeInfo.setStartTime(System.currentTimeMillis());
    }
    routeInfo.setKeepTime(keepTime);
    Long endTime = null;
    // 代表永久使用
    if (keepTime == -1L) {
      endTime = Long.MAX_VALUE;
    } else {
      endTime = startTime + 1000 * keepTime; // 计算出结束时间
    }
    routeInfo.setEndTime(endTime);
    if (routeInfoRepository.findRouteInfo(routeInfo) != null) {
      throw new PermissionFailException("请勿重复配置权限");
    }
    logger.info("==插入MongoDB的鉴权记录==" + routeInfo);
    routeInfoRepository.create(routeInfo); // 插入mongodb
    // 缓存用户与应用的关系，便于设备上报信息时鉴权使用
    Long currentTime = System.currentTimeMillis();
    // 该权限不是立刻生效
    if (startTime != null && startTime > currentTime) {
      logger.info("==配置的权限不是立刻生效，无需放入redis中==");
    }
    // 只有keepTime，立刻生效,startTime设置为currentTime
    else {
      logger.info("==配置的权限立刻生效，需放入redis中==");
      write2RedisService.writeRoute2Redis(routeInfo);
    }
    result = routeInfoRepository.findRouteInfo(routeInfo) != null;
    long end = System.currentTimeMillis();
    logger.info("--配置权限程序用时--：" + (end - start) + "ms");
    return result;
  }

  @Override
  public boolean removeConnectAuth(
      String accountId, String productionId, String deviceId, String userId, String applicationId) {
    long start = System.currentTimeMillis();
    if (!getPermisson(accountId, productionId, applicationId)) {
      throw new PermissionFailException("权限错误");
    }
    // 判断应用与产品之间是否有权限
    Route route = routeRepository.findByTwoId(productionId, applicationId);
    boolean result = false;
    // 应用与产品之间没有权限
    if (route == null) {
      throw new PermissionFailException("权限错误");
    }
    // 删除mogodb中鉴权的记录
    RouteInfo routeInfo = new RouteInfo();
    routeInfo.setAccountId(accountId);
    routeInfo.setProductionId(productionId);
    routeInfo.setDeviceId(deviceId);
    routeInfo.setUserId(userId);
    routeInfo.setApplicationId(applicationId);
    RouteInfo findRouteInfo = routeInfoRepository.findRouteInfo(routeInfo);
    logger.info("==要删除的鉴权记录为==" + findRouteInfo);
    routeInfoRepository.delete(findRouteInfo);
    String usingKey =
        RedisConstant.USING_KEY
            + applicationId
            + ":"
            + userId
            + ":"
            + productionId
            + ":"
            + deviceId;
    String reportKey =
        RedisConstant.REPORT_KEY
            + applicationId
            + ":"
            + userId
            + ":"
            + productionId
            + ":"
            + deviceId;
    redisService.delete(usingKey);
    redisService.delete(reportKey);
    long end = System.currentTimeMillis();
    result = routeInfoRepository.findRouteInfo(routeInfo) == null;
    logger.info("--移除权限程序用时--：" + (end - start) + "ms");
    return result;
  }

  /**
   * 判断是否有权限操作
   *
   * @param accountId
   * @param productionId
   * @param applicationId
   * @return
   */
  private boolean getPermisson(String accountId, String productionId, String applicationId) {
    Production production = productionRepository.findById(productionId);
    if (production == null) {
      return false;
    }
    if (!production.getAccountId().equals(accountId)) {
      return false;
    }
    ApplicationEntity applicationEntity = applicationRepository.findById(applicationId).get();
    if (applicationEntity == null) {
      return false;
    }
    if (!applicationEntity.getAccountId().equals(accountId)) {
      return false;
    }
    return true;
  }

  private boolean getDevPermission(String accountId, String productionId, String deviceId) {
    Device device = deviceRepository.findById(accountId, productionId, deviceId);
    if (device == null) {
      return false;
    }
    if (!device.getProductionId().equals(productionId)) {
      return false;
    }
    return true;
  }
}
