package com.warpfuture.repository.impl;

import com.warpfuture.entity.ReportUser;
import com.warpfuture.repository.ReportUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportUserRepositoryImpl implements ReportUserRepository {
  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public List<ReportUser> getUserIdList(String applicationId) {
    Query query = new Query(Criteria.where("applicationId").is(applicationId));
    return mongoTemplate.find(query, ReportUser.class);
  }
}
