package com.warpfuture.repository.impl;

import com.warpfuture.entity.HistoricalData;
import com.warpfuture.repository.HistoryDataHelper;
import com.warpfuture.repository.HistoryDataRepository;
import com.warpfuture.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/** @Auther: fido @Date: 2018/5/20 20:55 @Description: */
@Repository
public class HistoryDataRepositoryImpl implements HistoryDataRepository {

  @Autowired private CassandraTemplate cassandraTemplate;

  @Autowired private HistoryDataHelper historyDataHelper;

  @Override
  public void insert(HistoricalData historicalData) {
    cassandraTemplate.insert(historicalData);
  }

  @Override
  public List<HistoricalData> findByHistoryDataId(
      String historyDataId, Integer pageIndex, Integer pageSize) {
    Integer limit = PageUtils.getSkip(pageIndex, pageSize);
    List<HistoricalData> findList = null;
    if (limit != 0) {
      List<HistoricalData> skipList = historyDataHelper.findByHistoryDataId(historyDataId, limit);
      if (skipList != null && skipList.size() > 0) {
        HistoricalData lastData = skipList.get(skipList.size() - 1);
        findList =
            historyDataHelper.findByTokenOfId(
                lastData.getHistoryDataId(), lastData.getDataTime().getTime(), pageSize);
      }
    } else findList = historyDataHelper.findByHistoryDataId(historyDataId, pageSize);
    return findList;
  }

  @Override
  public List<HistoricalData> findByHistoryDataIdAndDataType(
      String historyDataId, Integer dataType, Integer pageIndex, Integer pageSize) {
    Integer limit = PageUtils.getSkip(pageIndex, pageSize);
    List<HistoricalData> findList = null;
    if (limit != 0) {
      List<HistoricalData> skipList =
          historyDataHelper.findByHistoryDataIdAndDataType(historyDataId, dataType, limit);
      if (skipList != null && skipList.size() > 0) {
        HistoricalData lastData = skipList.get(skipList.size() - 1);
        findList =
            historyDataHelper.findByTokenOfType(
                lastData.getHistoryDataId(), dataType, lastData.getDataTime().getTime(), pageSize);
      }
    } else
      findList =
          historyDataHelper.findByHistoryDataIdAndDataType(historyDataId, dataType, pageSize);
    return findList;
  }

  @Override
  public List<HistoricalData> findByHistoryDataIdAndDataTime(
      String historyDataId, Long starTime, Long endTime, Integer pageIndex, Integer pageSize) {
    Integer limit = PageUtils.getSkip(pageIndex, pageSize);
    List<HistoricalData> findList = null;
    if (limit != 0) {
      List<HistoricalData> skipList =
          historyDataHelper.findByHistoryDataIdAndDataTime(historyDataId, starTime, endTime, limit);
      if (skipList != null && skipList.size() > 0) {
        HistoricalData lastData = skipList.get(skipList.size() - 1);
        findList =
            historyDataHelper.findByTokenOfTime(
                lastData.getHistoryDataId(), lastData.getDataTime().getTime(), endTime, pageSize);
      }
    } else
      findList =
          historyDataHelper.findByHistoryDataIdAndDataTime(
              historyDataId, starTime, endTime, pageSize);
    return findList;
  }
}
