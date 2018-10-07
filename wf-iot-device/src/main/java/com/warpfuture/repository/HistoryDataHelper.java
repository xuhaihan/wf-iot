package com.warpfuture.repository;

import com.warpfuture.entity.HistoricalData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

/** @Auther: fido @Date: 2018/5/20 21:28 @Description: */
public interface HistoryDataHelper extends CassandraRepository<HistoricalData, String> {

  @Query("select count(*) from wf_iot_historydata where historyDataId=?0")
  Long countByHistoryDataId(String historyDataId);

  @Query(
      "select count(*) from wf_iot_historydata where historyDataId=?0 and dataType=?1 ALLOW FILTERING")
  Long countByHistoryDataIdAndAndDataType(String historyDataId, Integer dataType);

  @Query(
      "select count(*) from wf_iot_historydata where historyDataId=?0 and dataTime>=?1 and dataTime<=?2 ALLOW FILTERING")
  Long countByHistoryDataIdAndDataTimeBetween(String historyDataId, Long startTime, Long endTime);

  @Query("select * from wf_iot_historydata where historyDataId=?0 limit ?1")
  List<HistoricalData> findByHistoryDataId(String historyDataId, Integer limit);

  @Query(
      "select * from wf_iot_historydata where historyDataId=?0 and dataType=?1 limit ?2 ALLOW FILTERING")
  List<HistoricalData> findByHistoryDataIdAndDataType(
      String historyDataId, Integer dataType, Integer limit);

  @Query(
      "select * from wf_iot_historydata where historyDataId=?0 and dataTime>?1 and dataTime<?2 limit ?3 ALLOW FILTERING")
  List<HistoricalData> findByHistoryDataIdAndDataTime(
      String historyDataId, Long startTime, Long endTime, Integer limit);

  @Query(
      "SELECT * FROM wf_iot_historydata WHERE TOKEN(historyDataId) = TOKEN(?0) AND (dataTime) > (?1) limit ?2 ALLOW FILTERING")
  List<HistoricalData> findByTokenOfId(String historyDataId, Long dataTime, Integer limit);

  @Query(
      "SELECT * FROM wf_iot_historydata WHERE  TOKEN(historyDataId) = TOKEN(?0) and (dataTime) >= (?2) and (dataType)=(?1) limit ?3 ALLOW FILTERING")
  List<HistoricalData> findByTokenOfType(
      String historyDataId, Integer dataType, Long dataTime, Integer limit);

  @Query(
      "SELECT * FROM wf_iot_historydata WHERE  TOKEN(historyDataId) = TOKEN(?0) and (dataTime) >= (?1) and (dataTime) <= (?2) limit ?3 ALLOW FILTERING")
  List<HistoricalData> findByTokenOfTime(
      String historyDataId, Long startTime, Long endTime, Integer limit);
}
