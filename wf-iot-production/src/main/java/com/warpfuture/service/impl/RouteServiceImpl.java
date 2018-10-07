package com.warpfuture.service.impl;

import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.entity.Production;
import com.warpfuture.entity.Route;
import com.warpfuture.exception.ParameterIllegalException;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.exception.RouteExistException;
import com.warpfuture.repository.ApplicationRepository;
import com.warpfuture.repository.ProductionRepository;
import com.warpfuture.repository.RouteRepository;
import com.warpfuture.service.RouteService;
import com.warpfuture.util.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** Created by fido on 2018/4/29. */
@Service
@Slf4j
public class RouteServiceImpl implements RouteService {
  @Autowired private RouteRepository routeRepository;
  @Autowired private ApplicationRepository applicationRepository;
  @Autowired private ProductionRepository productionRepository;

  @Override
  public Route createRoute(Route route) {
    String applicationId = route.getApplicationId();
    String productionId = route.getProductionId();
    ApplicationEntity applicationEntity = applicationRepository.findById(applicationId).get();
    Production production = productionRepository.findByProductionId(productionId);
    if (applicationEntity == null || production == null) {
      throw new ParameterIllegalException("参数有误");
    }
    if (!route.getAccountId().equals(applicationEntity.getAccountId())
        || !route.getAccountId().equals(production.getAccountId())) {
      throw new PermissionFailException("创建路由配置权限有误");
    }
    if (!this.legal(applicationId, productionId)) {
      throw new RouteExistException("该路由配置已存在");
    }
    String routeId = IdUtils.getId();
    route.setRouteId(routeId);
    route.setCreateTime(System.currentTimeMillis());
    route.setUpdateTime(System.currentTimeMillis());
    return routeRepository.create(route);
  }

  @Override
  public Route updateRoute(Route route) {
    String accountId = route.getAccountId();
    Route findRoute =
        routeRepository.findByTwoId(route.getApplicationId(), route.getProductionId());
    if (!findRoute.getAccountId().equals(accountId)) {
      throw new PermissionFailException("更新路由配置权限错误");
    }
    findRoute.setBroadcast(route.getBroadcast());
    findRoute.setRelation(route.getRelation());
    findRoute.setUpdateTime(System.currentTimeMillis());
    return routeRepository.update(findRoute);
  }

  /**
   * 判断是否配置了重复的路由配置
   *
   * @param applicationId
   * @param productionId
   * @return
   */
  private boolean legal(String applicationId, String productionId) {
    Route route = routeRepository.findByTwoId(applicationId, productionId);
    if (route == null) {
      return true;
    } else return false;
  }

  @Override
  public void deleteRoute(String accountId, String applicationId, String productionId) {
    Route route = routeRepository.findByTwoId(applicationId, productionId);
    if (route != null) {
      if (!route.getAccountId().equals(accountId)) {
        throw new PermissionFailException("删除路由配置权限有错");
      }
      routeRepository.deleteByTwoId(applicationId, productionId);
    }
  }

  @Override
  public List<Route> findByApplicationId(String accountId, String applicationId) {
    ApplicationEntity applicationEntity = applicationRepository.findById(applicationId).get();
    if (applicationEntity == null) {
      throw new ParameterIllegalException("参数有误");
    }
    if (!applicationEntity.getAccountId().equals(accountId)) {
      throw new PermissionFailException("权限有误");
    }
    List<Route> list = routeRepository.findByApplicationId(applicationId);
    return list;
  }

  @Override
  public List<Route> createRouteByApplicationId(
      String accountId, String applicationId, List<Route> wait2CreateList) {
    log.info("==接收创建路由的参数==" + accountId + "," + applicationId + "," + wait2CreateList);
    ApplicationEntity applicationEntity = applicationRepository.findById(applicationId).get();
    if (applicationEntity == null) {
      throw new ParameterIllegalException("参数有误");
    }
    if (!applicationEntity.getAccountId().equals(accountId)) {
      throw new PermissionFailException("权限有误");
    }
    List<Route> list = routeRepository.findAllByApplicationId(applicationId);
    // 该应用没有路由配置
    if (wait2CreateList == null || wait2CreateList.size() == 0) {
      if (list != null && list.size() > 0) {
        for (Route route : list) {
          routeRepository.delete(route);
        }
      }
    } else {
      // 遍历list，增加/修改路由配置记录
      for (Route route : wait2CreateList) {
        // 没有配过
        if (list == null || list.size() == 0) {
          route.setRouteId(IdUtils.getId());
          route.setAccountId(accountId);
          route.setApplicationId(applicationId);
          route.setCreateTime(System.currentTimeMillis());
          route.setUpdateTime(System.currentTimeMillis());
          route.setIsDelete(false);
          routeRepository.create(route);
        }
        // 已经配过了，需要修改
        else {
          List<Route> newRouteList = new ArrayList<>();
          for (Route route1 : wait2CreateList) {
            route1.setApplicationId(applicationId);
            route1.setAccountId(accountId);
            newRouteList.add(route1);
          }
          list.removeAll(newRouteList); // 需要删除
          for (Route route1 : list) {
            if (!route1.getIsDelete()) routeRepository.delete(route1);
          }
          // 剩下的就是需要重新save
          for (Route route1 : wait2CreateList) {
            Route newRoute = routeRepository.findByTwoId(applicationId, route1.getProductionId());
            if (newRoute != null) {
              newRoute.setIsDelete(false);
              newRoute.setBroadcast(route1.getBroadcast());
              newRoute.setRelation(route1.getRelation());
              newRoute.setUpdateTime(System.currentTimeMillis());
              routeRepository.update(newRoute);
            } else {
              route1.setRouteId(IdUtils.getId());
              route1.setIsDelete(false);
              route1.setCreateTime(System.currentTimeMillis());
              routeRepository.create(route1);
            }
          }
        }
      }
    }
    return routeRepository.findByApplicationId(applicationId);
  }
}
