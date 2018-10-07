package com.warpfuture.service;

import com.warpfuture.entity.Route;

import java.util.List;

/** Created by fido on 2018/4/29. */
public interface RouteService {

  public Route createRoute(Route route);

  public Route updateRoute(Route route);

  public void deleteRoute(String accountId, String applicationId, String productionId);

  public List<Route> findByApplicationId(String accountId, String applicationId);

  public List<Route> createRouteByApplicationId(
      String accountId, String applicationId, List<Route> wait2CreateList);
}
