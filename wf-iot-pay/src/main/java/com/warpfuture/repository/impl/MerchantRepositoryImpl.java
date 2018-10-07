package com.warpfuture.repository.impl;

import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.merchant.WxpayData;
import com.warpfuture.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Created by fido on 2018/4/17. */
@Repository
public class MerchantRepositoryImpl implements MerchantRepository {

  @Autowired private MongoTemplate mongoTemplate;

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
  public WxpayData getByMerchantId(String merchantId) {
    Merchant merchant = this.findById(merchantId);
    if (merchant != null) {
      return merchant.getWxPayData();
    }
    return null;
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
