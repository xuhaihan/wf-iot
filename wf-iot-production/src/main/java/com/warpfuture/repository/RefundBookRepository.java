package com.warpfuture.repository;

import com.warpfuture.entity.RefundBook;
import org.springframework.data.mongodb.repository.MongoRepository;

/** @Auther: fido @Date: 2018/6/6 16:09 @Description: 退款证书的管理 */
public interface RefundBookRepository extends MongoRepository<RefundBook, String> {}

