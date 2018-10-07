package com.warpfuture.repository.impl;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.repository.ProductionRepository;
import com.warpfuture.util.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/13. */
@Repository
public class ProductionRepositoryImpl implements ProductionRepository {

  @Autowired private MongoTemplate mongoTemplate;

  private Logger logger = LoggerFactory.getLogger(ProductionRepositoryImpl.class);

  @Override
  public PageModel<Production> findByAccountId(
      String accountId, Integer pageSize, Integer pageIndex) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("accountId").is(accountId), Criteria.where("isDelete").is(false));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Production.class); // 总记录数
    PageModel pageModel = new PageModel();
    pageModel.setRowCount((int) rowCount);
    pageModel.setPageIndex(pageIndex);
    pageModel.setPageSize(pageSize);
    pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Production> result = mongoTemplate.find(query, Production.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public Production findByProductionId(String productionId) {
    return mongoTemplate.findById(productionId, Production.class);
  }

  @Override
  public void saveProduction(Production production) {
    mongoTemplate.insert(production);
  }

  @Override
  public void updateProductionKey(String productionId, String productionKey) {
    Query query = new Query(Criteria.where("productionId").is(productionId));
    Update update =
        new Update()
            .set("productionKey", productionKey)
            .set("updateTime", System.currentTimeMillis());
    mongoTemplate.updateFirst(query, update, Production.class);
  }

  @Override
  public void regenKeyPair(
      String productionId, String productionPublicSecure, String productionPrivateSecure) {
    Query query = new Query();
    query.addCriteria(Criteria.where("productionId").is(productionId));
    Update update =
        new Update()
            .set("productionPublicSecure", productionPublicSecure)
            .set("productionPrivateSecure", productionPrivateSecure)
            .set("updateTime", System.currentTimeMillis());
    mongoTemplate.updateFirst(query, update, Production.class);
  }

  @Override
  public void updateProductionInfo(Production production) {
    mongoTemplate.save(production);
  }

  @Override
  public void publishProduction(String productionId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("productionId").is(productionId));
    Update update =
        new Update().set("productionStatus", 1).set("updateTime", System.currentTimeMillis());
    mongoTemplate.updateFirst(query, update, Production.class);
  }

  @Override
  public void revokeProduction(String productionId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("productionId").is(productionId));
    Update update =
        new Update().set("productionStatus", 2).set("updateTime", System.currentTimeMillis());
    mongoTemplate.updateFirst(query, update, Production.class);
  }

  @Override
  public void deleteProduction(String productionId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("productionId").is(productionId));
    Update update =
        new Update().set("isDelete", true).set("updateTime", System.currentTimeMillis());
    mongoTemplate.updateFirst(query, update, Production.class);
  }

  @Override
  public PageModel<Production> findByName(
      String accountId, String productionName, Integer pageSize, Integer pageIndex) {
    Criteria criatira = new Criteria();
    // 根据name模糊查询
    criatira.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("productionName").regex(productionName),
        Criteria.where("isDelete").is(false));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Production.class); // 总记录数
    PageModel pageModel = new PageModel();
    pageModel.setRowCount((int) rowCount);
    pageModel.setPageIndex(pageIndex);
    pageModel.setPageSize(pageSize);
    pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Production> result = mongoTemplate.find(query, Production.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public Production findByProductionKey(String productionKey) {
    Query query = new Query(Criteria.where("productionKey").is(productionKey));
    return mongoTemplate.findOne(query, Production.class);
  }

  @Override
  public Production getByProductionName(String accountId, String productionName) {
    Criteria criatira = new Criteria();
    // 根据name模糊查询
    criatira.andOperator(
        Criteria.where("accountId").is(accountId),
        Criteria.where("productionName").is(productionName));
    Query query = new Query(criatira);
    return mongoTemplate.findOne(query, Production.class);
  }
}
