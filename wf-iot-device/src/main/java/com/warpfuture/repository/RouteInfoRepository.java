package com.warpfuture.repository;

import com.warpfuture.entity.RouteInfo;

import java.util.List;

/** Created by fido on 2018/5/11. */
public interface RouteInfoRepository {

  public void create(RouteInfo routeInfo);

  public void delete(RouteInfo routeInfo);

  /**
   * 根据accountId,applicationId,productionId,userId,applicationId查找鉴权记录
   *
   * @param routeInfo
   * @return
   */
  public RouteInfo findRouteInfo(RouteInfo routeInfo);

  /**
   * 根据productionId和deviceId查找鉴权记录
   *
   * @param deviceId
   * @param productionId
   * @return
   */
  public List<RouteInfo> findByDevice(String deviceId, String productionId);

  /**
   * 根据applicationId,productionId,userId,applicationId查找鉴权记录
   *
   * @param productionId
   * @param applicationId
   * @param deviceId
   * @param userId
   * @return
   */
  public RouteInfo findByUser(
      String productionId, String applicationId, String deviceId, String userId);

  /**
   * 找出过期的鉴权记录，进行删除
   *
   * @param currentTime
   * @return
   */
  public List<RouteInfo> findOutOfDateRoute(long currentTime);

  /**
   * 找到目前有的所有鉴权记录
   *
   * @return
   */
  public List<RouteInfo> getAllRoute();
}
