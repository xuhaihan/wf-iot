package com.warpfuture.iot.oauth.repository.impl;

import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.iot.oauth.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/** Created by 徐海瀚 on 2018/4/17. */
@Repository
public class ApplicationRepositoryImpl extends BaseRepositoryImpl<ApplicationEntity>
    implements ApplicationRepository {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  protected Class<ApplicationEntity> getEntityClass() {
    return ApplicationEntity.class;
  }
}
