package com.warpfuture.repository;

import com.warpfuture.entity.Route;

import java.util.List;

/** Created by fido on 2018/4/29. */
public interface RouteRepository {
  /**
   * 创建新的路由配置
   *
   * @param route
   * @return
   */
  public Route create(Route route);

  /**
   * 更新路由配置
   *
   * @param route
   * @return
   */
  public Route update(Route route);

  /**
   * 根据两个id查找路由配置
   *
   * @param applicationId
   * @param productionId
   * @return
   */
  public Route findByTwoId(String applicationId, String productionId);

  /**
   * 根据id查找路由配置
   *
   * @param routeId
   * @return
   */
  public Route findById(String routeId);

  /**
   * 根据两个id删除路由配置
   *
   * @param applicationId
   * @param productionId
   */
  public void deleteByTwoId(String applicationId, String productionId);

  /**
   * 删除路由配置
   *
   * @param route
   */
  public void delete(Route route);

  /**
   * 查找跟应用相关的路由配置
   *
   * @param applicationId
   * @return
   */
  public List<Route> findByApplicationId(String applicationId);

  /**
   * 查找跟应用有关的路由配置，包括删除的
   *
   * @param applicationId
   * @return
   */
  public List<Route> findAllByApplicationId(String applicationId);
}
