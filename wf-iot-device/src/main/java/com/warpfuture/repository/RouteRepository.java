package com.warpfuture.repository;

import com.warpfuture.entity.Route;

import java.util.List;

/** Created by fido on 2018/4/14. 鉴权表 */
public interface RouteRepository {
  public Route findByTwoId(String productionId, String applicationId);

  public List<Route> findByProductionId(String productionId);

  public List<Route> findByProAndBroadcast(String productionId);
}
