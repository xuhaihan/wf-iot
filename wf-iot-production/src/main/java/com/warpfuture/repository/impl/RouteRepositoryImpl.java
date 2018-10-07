package com.warpfuture.repository.impl;

import com.mongodb.Mongo;
import com.warpfuture.entity.Route;
import com.warpfuture.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/29. */
@Repository
public class RouteRepositoryImpl implements RouteRepository {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public Route create(Route route) {
    route.setIsDelete(false);
    mongoTemplate.insert(route);
    return this.findByTwoId(route.getApplicationId(), route.getProductionId());
  }

  @Override
  public Route update(Route route) {
    mongoTemplate.save(route);
    return this.findByTwoId(route.getApplicationId(), route.getProductionId());
  }

  @Override
  public Route findByTwoId(String applicationId, String productionId) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("productionId").is(productionId),
        Criteria.where("applicationId").is(applicationId));
    Query query = new Query(criatira);
    return mongoTemplate.findOne(query, Route.class);
  }

  @Override
  public Route findById(String routeId) {
    return mongoTemplate.findById(routeId, Route.class);
  }

  @Override
  public void delete(Route route) {
    if (route != null) {
      route.setUpdateTime(System.currentTimeMillis());
      route.setIsDelete(true);
      mongoTemplate.save(route);
    }
  }

  @Override
  public void deleteByTwoId(String applicationId, String productionId) {
    Route route = this.findByTwoId(applicationId, productionId);
    route.setUpdateTime(System.currentTimeMillis());
    route.setIsDelete(true);
    mongoTemplate.save(route);
  }

  @Override
  public List<Route> findByApplicationId(String applicationId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("applicationId").is(applicationId), Criteria.where("isDelete").is(false));
    Query query = new Query(criteria);
    List<Route> list = mongoTemplate.find(query, Route.class);
    return list;
  }

  @Override
  public List<Route> findAllByApplicationId(String applicationId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(Criteria.where("applicationId").is(applicationId));
    Query query = new Query(criteria);
    List<Route> list = mongoTemplate.find(query, Route.class);
    return list;
  }
}
