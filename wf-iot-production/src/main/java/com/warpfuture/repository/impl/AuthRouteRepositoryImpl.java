package com.warpfuture.repository.impl;

import com.warpfuture.entity.RouteInfo;
import com.warpfuture.repository.AuthRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** @Auther: fido @Date: 2018/6/28 17:48 @Description: 返回设备相关的使用信息 */
@Repository
public class AuthRouteRepositoryImpl implements AuthRouteRepository {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public List<RouteInfo> findDeviceAuthRecord(
      String accountId, String productionId, String deviceId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("productionId").is(productionId),
        Criteria.where("deviceId").is(deviceId));
    Query query = new Query(criteria);
    List<RouteInfo> result = mongoTemplate.find(query, com.warpfuture.entity.RouteInfo.class);
    return result;
  }
}
