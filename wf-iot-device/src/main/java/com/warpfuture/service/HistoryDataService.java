package com.warpfuture.service;

import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.HistoricalData;
import com.warpfuture.entity.HistoryDataPageModel;

/** Created by fido on 2018/5/14. */
public interface HistoryDataService {

  /**
   * 插入历史记录
   *
   * @param historicalData
   */
  public void insert(HistoricalData historicalData);

  /**
   * 分页查询历史数据
   *
   * @param dataInfo
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public HistoryDataPageModel<HistoricalData> findByHistoryDataId(
      HistoryDataInfo dataInfo, Integer pageIndex, Integer pageSize);

  /**
   * 根据类型来查
   *
   * @param dataInfo
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public HistoryDataPageModel<HistoricalData> findByHistoryDataIdAndDataType(
      HistoryDataInfo dataInfo, Integer pageIndex, Integer pageSize);

  /**
   * 根据时间来查询
   *
   * @param dataInfo
   * @return
   */
  public HistoryDataPageModel<HistoricalData> findByHistoryDataIdAndDataTime(
      HistoryDataInfo dataInfo, Integer pageIndex, Integer pageSize);
}
