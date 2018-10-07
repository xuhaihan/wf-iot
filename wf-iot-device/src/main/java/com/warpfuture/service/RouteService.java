package com.warpfuture.service;

import java.util.List;

/** Created by fido on 2018/4/14. 鉴权服务 */
public interface RouteService {

  /**
   * 设备到用户的鉴权
   *
   * @param productionId 产品Id
   * @param deviceId 设备Id
   * @return 返回用户列表
   */
  public List<String> devToUser(String productionId, String deviceId);

  /**
   * 用户到设备的鉴权
   *
   * @param applicationId
   * @param productionId
   * @param deviceId
   * @param userId
   * @return
   */
  public boolean userToDev(
      String applicationId, String productionId, String deviceId, String userId);
}
