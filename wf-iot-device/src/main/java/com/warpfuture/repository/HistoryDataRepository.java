package com.warpfuture.repository;

import com.warpfuture.entity.HistoricalData;

import java.util.List;

/** Created by fido on 2018/5/14. */
public interface HistoryDataRepository {

  /**
   * 插入一条设备通信历史记录
   *
   * @param historicalData
   */
  public void insert(HistoricalData historicalData);

  /**
   * 分页查询某个设备的通信数据
   *
   * @param historyDataId
   * @param pageIndex
   * @param pageSize
   * @return
   */
  List<HistoricalData> findByHistoryDataId(
      String historyDataId, Integer pageIndex, Integer pageSize);

  /**
   * 分页查询某个设备下的某个分类的通信数据
   *
   * @param historyDataId
   * @param dataType
   * @param pageIndex
   * @param pageSize
   * @return
   */
  List<HistoricalData> findByHistoryDataIdAndDataType(
      String historyDataId, Integer dataType, Integer pageIndex, Integer pageSize);

  /**
   * 根据时间段查询
   *
   * @param historyDataId
   * @param starTime
   * @param endTime
   * @param pageIndex
   * @param pageSize
   * @return
   */
  List<HistoricalData> findByHistoryDataIdAndDataTime(
      String historyDataId, Long starTime, Long endTime, Integer pageIndex, Integer pageSize);
}
