package com.warpfuture.repository.impl;

import com.warpfuture.entity.RouteInfo;
import com.warpfuture.repository.RouteInfoRepository;
import com.warpfuture.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/5/11. */
@Repository
public class RouteInfoRepositoryImpl implements RouteInfoRepository {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void create(RouteInfo routeInfo) {
    routeInfo.setRouteInfoId(IdUtils.getId());
    mongoTemplate.insert(routeInfo);
  }

  @Override
  public void delete(RouteInfo routeInfo) {
    RouteInfo result = this.findRouteInfo(routeInfo);
    if (result != null) mongoTemplate.remove(result);
  }

  @Override
  public RouteInfo findRouteInfo(RouteInfo routeInfo) {
    String accountId = routeInfo.getAccountId();
    String productionId = routeInfo.getProductionId();
    String applicationId = routeInfo.getApplicationId();
    String deviceId = routeInfo.getDeviceId();
    String userId = routeInfo.getUserId();
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("productionId").is(productionId),
        Criteria.where("applicationId").is(applicationId),
        Criteria.where("deviceId").is(deviceId),
        Criteria.where("userId").is(userId));
    Query query = new Query(criteria);
    return mongoTemplate.findOne(query, RouteInfo.class);
  }

  @Override
  public List<RouteInfo> findByDevice(String deviceId, String productionId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("productionId").is(productionId), Criteria.where("deviceId").is(deviceId));
    Query query = new Query(criteria);
    return mongoTemplate.find(query, RouteInfo.class);
  }

  @Override
  public RouteInfo findByUser(
      String productionId, String applicationId, String deviceId, String userId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("productionId").is(productionId),
        Criteria.where("applicationId").is(applicationId),
        Criteria.where("deviceId").is(deviceId),
        Criteria.where("userId").is(userId));
    Query query = new Query(criteria);
    return mongoTemplate.findOne(query, RouteInfo.class);
  }

  @Override
  public List<RouteInfo> findOutOfDateRoute(long currentTime) {
    Query query = new Query(Criteria.where("endTime").lt(currentTime));
    List<RouteInfo> list = mongoTemplate.find(query, RouteInfo.class);
    return list;
  }

  @Override
  public List<RouteInfo> getAllRoute() {
    return mongoTemplate.findAll(RouteInfo.class);
  }
}
