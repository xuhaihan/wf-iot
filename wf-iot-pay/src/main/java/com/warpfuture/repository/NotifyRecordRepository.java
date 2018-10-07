package com.warpfuture.repository;

import com.warpfuture.entity.NotifyRecord;
import org.springframework.data.cassandra.repository.CassandraRepository;

/** @Auther: fido @Date: 2018/5/23 11:07 @Description: */
public interface NotifyRecordRepository extends CassandraRepository<NotifyRecord, String> {}
