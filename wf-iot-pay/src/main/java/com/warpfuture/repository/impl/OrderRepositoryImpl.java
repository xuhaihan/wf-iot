package com.warpfuture.repository.impl;

import com.warpfuture.constant.OrderConstant;
import com.warpfuture.entity.order.Order;
import com.warpfuture.repository.OrderRepository;
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
  public Order findByMerchantTradeNumber(String merchantId, String merchantTradeNumber) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("merchantId").is(merchantId),
        Criteria.where("merchantTradeNumber").is(merchantTradeNumber));
    Query query = new Query(criteria);
    return mongoTemplate.findOne(query, Order.class);
  }

  @Override
  public void insert(Order order) {
    mongoTemplate.insert(order);
  }

  @Override
  public void save(Order order) {
    mongoTemplate.save(order);
  }

  @Override
  public Order findById(String orderId) {
    return mongoTemplate.findById(orderId, Order.class);
  }

  @Override
  public List<Order> getPreparToQueryList() {
    Query query = new Query(Criteria.where("status").is(0));
    return mongoTemplate.find(query, Order.class);
  }

  @Override
  public List<Order> getOverTimeWxQrder() {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("status").is(OrderConstant.ORDER_STATUS_READY),
        Criteria.where("tradeType").is(OrderConstant.ORDER_WXTRADE),
        Criteria.where("createTime")
            .lt(System.currentTimeMillis() - OrderConstant.ORDER_OVER_TIME));
    Query query = new Query(criteria);
    List<Order> overTimeOrderList = mongoTemplate.find(query, Order.class);
    return overTimeOrderList;
  }

  @Override
  public List<Order> getOverTimeWxRefund() {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("status").is(OrderConstant.ORDER_STATUS_REQUIRE_REFUND),
        Criteria.where("tradeType").is(OrderConstant.ORDER_WXTRADE),
        Criteria.where("updateTime")
            .lt(System.currentTimeMillis() - OrderConstant.ORDER_OVER_TIME));
    Query query = new Query(criteria);
    List<Order> overTimeOrderList = mongoTemplate.find(query, Order.class);
    return overTimeOrderList;
  }
}
