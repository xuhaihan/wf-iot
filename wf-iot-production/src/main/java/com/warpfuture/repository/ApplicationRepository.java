package com.warpfuture.repository;

import com.warpfuture.entity.ApplicationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** Created by fido on 2018/4/20. 用于权限校验 account与application之间是否存在关系 */
@Repository
public interface ApplicationRepository extends MongoRepository<ApplicationEntity, String> {}
