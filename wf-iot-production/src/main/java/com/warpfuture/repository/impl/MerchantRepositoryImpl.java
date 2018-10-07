package com.warpfuture.repository.impl;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.repository.MerchantRepository;
import com.warpfuture.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/17. */
@Repository
public class MerchantRepositoryImpl implements MerchantRepository {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void create(Merchant merchant) {
    mongoTemplate.insert(merchant);
  }

  @Override
  public void delete(String merchantId) {
    Merchant merchant = mongoTemplate.findById(merchantId, Merchant.class);
    if (merchant != null) {
      merchant.setIsDelete(true);
      merchant.setUpdateTime(System.currentTimeMillis());
      mongoTemplate.save(merchant);
    }
  }

  @Override
  public void update(Merchant merchant) {
    mongoTemplate.save(merchant);
  }

  @Override
  public Merchant findById(String merchantId) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("merchantId").is(merchantId), Criteria.where("isDelete").is(false));
    Query query = new Query(criatira);
    Merchant merchant = mongoTemplate.findOne(query, Merchant.class);
    return merchant;
  }

  @Override
  public PageModel<Merchant> findByAccountId(
      String accountId, Integer pageIndex, Integer pageSize) {
    Criteria criatira = new Criteria();
    criatira.andOperator(
        Criteria.where("accountId").is(accountId), Criteria.where("isDelete").is(false));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Merchant.class); // 总记录数
    PageModel pageModel = PageUtils.dealOverPage(pageIndex, pageSize, (int) rowCount);
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Merchant> result = mongoTemplate.find(query, Merchant.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public Merchant findByAppIdAndMchId(String appId, String wxPayMerchantId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("wxPayData.wxPayMerchantId").is(wxPayMerchantId),
        Criteria.where("wxPayData.wxPayAppId").is(appId),
        Criteria.where("isDelete").is(false));
    Query query = new Query(criteria);
    return mongoTemplate.findOne(query, Merchant.class);
  }
}
