package com.warpfuture.repository.impl;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.order.Order;
import com.warpfuture.repository.OrderRepository;
import com.warpfuture.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by fido on 2018/4/26. */
@Repository
public class OrderRepositoryImpl implements OrderRepository {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public PageModel<Order> findByMerchantId(String merchantId, Integer pageIndex, Integer pageSize) {
    Criteria criatira = new Criteria();
    criatira.andOperator(Criteria.where("merchantId").is(merchantId));
    Query query = new Query(criatira);
    long rowCount = mongoTemplate.count(query, Order.class); // 总记录数
    PageModel pageModel = new PageModel();
    pageModel.setRowCount((int) rowCount);
    pageModel.setPageIndex(pageIndex);
    pageModel.setPageSize(pageSize);
    pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
    query.skip(pageModel.getSkip()).limit(pageSize);
    List<Order> result = mongoTemplate.find(query, Order.class);
    pageModel.setData(result);
    return pageModel;
  }

  @Override
  public Order findByMerchantTradeNumber(String merchantId, String merchantTradeNumber) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("merchantId").is(merchantId),
        Criteria.where("merchantTradeNumber").is(merchantTradeNumber));
    Query query = new Query(criteria);
    return mongoTemplate.findOne(query, Order.class);
  }
}
