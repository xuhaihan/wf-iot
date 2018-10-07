package com.warpfuture.repository.impl;

import com.warpfuture.entity.RefundBook;
import com.warpfuture.repository.RefundBookRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/** @Auther: fido @Date: 2018/6/5 21:56 @Description: */
@Repository
public class RefundBookRepositoryImpl implements RefundBookRepostiory {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public void insert(RefundBook refundBook) {
    mongoTemplate.insert(refundBook);
  }

  @Override
  public RefundBook findById(String refundLocation) {
    return mongoTemplate.findById(refundLocation, RefundBook.class);
  }
}
