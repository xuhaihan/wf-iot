package com.warpfuture.repository.impl;

import com.warpfuture.constant.Constant;
import com.warpfuture.entity.OTAInfo;
import com.warpfuture.entity.PageModel;
import com.warpfuture.repository.OTARepository;
import com.warpfuture.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/26. */
@Repository
public class OTARepositoryImpl implements OTARepository {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void createOTA(OTAInfo otaInfo) {
    mongoTemplate.insert(otaInfo);
  }

  @Override
  public PageModel<OTAInfo> queryByProductionId(
      String productionId, Integer pageSize, Integer pageIndex) {
    Criteria criatira = new Criteria();
    criatira.andOperator(Criteria.where("productionId").is(productionId));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, OTAInfo.class); // 总记录数
    PageModel pageModel = new PageModel();
    pageModel.setRowCount((int) rowCount);
    pageModel.setPageIndex(pageIndex);
    pageModel.setPageSize(pageSize);
    pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<OTAInfo> result = mongoTemplate.find(query, OTAInfo.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public void diable(String otaId) {
    Query query = new Query(Criteria.where("otaId").is(otaId));
    Update update = new Update().set("otaStatus", Constant.OTA_DISABLED);
    mongoTemplate.updateFirst(query, update, OTAInfo.class);
  }

  @Override
  public OTAInfo queryByOTAId(String otaId) {
    return mongoTemplate.findById(otaId, OTAInfo.class);
  }

  @Override
  public OTAInfo getTheLatestOta(String productionId) {
    Query query = new Query(Criteria.where("productionId").is(productionId));
    query.with(new Sort(Sort.Direction.DESC, "createTime")).limit(1);
    return mongoTemplate.findOne(query, OTAInfo.class);
  }
}
