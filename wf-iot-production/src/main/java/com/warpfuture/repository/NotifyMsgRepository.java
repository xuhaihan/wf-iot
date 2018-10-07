package com.warpfuture.repository;

import com.warpfuture.entity.NotifyMsg;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: fido
 * @Date: 2018/6/12 00:13
 * @Description:
 */
public interface NotifyMsgRepository extends MongoRepository<NotifyMsg,String> {
}
