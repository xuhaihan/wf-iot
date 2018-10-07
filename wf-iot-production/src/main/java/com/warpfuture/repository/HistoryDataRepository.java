package com.warpfuture.repository;

import com.warpfuture.entity.HistoricalData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

/** @Auther: fido @Date: 2018/5/20 21:28 @Description: */
public interface HistoryDataRepository extends CassandraRepository<HistoricalData, String> {}
