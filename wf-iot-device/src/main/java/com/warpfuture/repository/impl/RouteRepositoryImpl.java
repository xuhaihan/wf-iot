package com.warpfuture.repository.impl;

import com.warpfuture.entity.Route;
import com.warpfuture.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/17. */
@Repository
public class RouteRepositoryImpl implements RouteRepository {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public Route findByTwoId(String productionId, String applicationId) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("productionId").is(productionId),
        Criteria.where("applicationId").is(applicationId),
        Criteria.where("isDelete").is(false));
    Query query = new Query(criatira);
    return mongoTemplate.findOne(query, Route.class);
  }

  @Override
  public List<Route> findByProductionId(String productionId) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("productionId").is(productionId), Criteria.where("isDelete").is(false));
    Query query = new Query(criatira);
    return mongoTemplate.find(query, Route.class);
  }

  @Override
  public List<Route> findByProAndBroadcast(String productionId) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("productionId").is(productionId),
        Criteria.where("broadcast").in(true),
        Criteria.where("isDelete").is(false));
    Query query = new Query(criatira);
    return mongoTemplate.find(query, Route.class);
  }
}
