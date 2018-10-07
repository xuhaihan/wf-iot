package com.warpfuture.repository.impl;

import com.warpfuture.entity.Production;
import com.warpfuture.repository.FindProductInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Created by fido on 2018/4/13. 设备登录认证查找产品信息 */
@Repository
public class FindProductInfoRepositoryImpl implements FindProductInfoRepository {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public Production findByProductionKey(String productionKey) {
    Query query = new Query(Criteria.where("productionKey").is(productionKey));
    return mongoTemplate.findOne(query, Production.class);
  }

  @Override
  public Production findByPPS(String productionPublicSecure) {
    Query query = new Query(Criteria.where("productionPublicSecure").is(productionPublicSecure));
    return mongoTemplate.findOne(query, Production.class);
  }

  @Override
  public Production findById(String productionId) {
    Query query = new Query(Criteria.where("productionId").is(productionId));
    return mongoTemplate.findOne(query, Production.class);
  }
}
