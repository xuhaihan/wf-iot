package com.warpfuture.service.impl;

import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.HistoricalData;
import com.warpfuture.entity.HistoryDataPageModel;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.DeviceRepository;
import com.warpfuture.repository.HistoryDataHelper;
import com.warpfuture.repository.HistoryDataRepository;
import com.warpfuture.service.HistoryDataService;
import com.warpfuture.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Created by fido on 2018/5/14. */
@Service
public class HistoryDataServiceImpl implements HistoryDataService {
  @Autowired private HistoryDataRepository historyDataRepository;
  @Autowired private DeviceRepository deviceRepository;
  @Autowired private HistoryDataHelper historyDataHelper;

  @Override
  public void insert(HistoricalData historicalData) {
    historyDataRepository.insert(historicalData);
  }

  @Override
  public HistoryDataPageModel<HistoricalData> findByHistoryDataId(
      HistoryDataInfo dataInfo, Integer pageIndex, Integer pageSize) {
    String accountId = dataInfo.getAccountId();
    String productionId = dataInfo.getProductionId();
    String devcieId = dataInfo.getDeviceId();
    if (!this.getPermission(accountId, productionId, devcieId)) {
      throw new PermissionFailException("查询权限有误");
    }
    String historicalDataId = accountId + ":" + productionId + ":" + devcieId;
    long rowCount = historyDataHelper.countByHistoryDataId(historicalDataId);
    HistoryDataPageModel<HistoricalData> historyDataPageModel =
        PageUtils.dealOverPage(pageIndex, pageSize, rowCount);
    List<HistoricalData> list =
        historyDataRepository.findByHistoryDataId(historicalDataId, pageIndex, pageSize);
    historyDataPageModel.setData(list);
    return historyDataPageModel;
  }

  @Override
  public HistoryDataPageModel<HistoricalData> findByHistoryDataIdAndDataType(
      HistoryDataInfo dataInfo, Integer pageIndex, Integer pageSize) {
    String accountId = dataInfo.getAccountId();
    String productionId = dataInfo.getProductionId();
    String devcieId = dataInfo.getDeviceId();
    if (!this.getPermission(accountId, productionId, devcieId)) {
      throw new PermissionFailException("查询权限有误");
    }
    String historicalDataId = accountId + ":" + productionId + ":" + devcieId;
    long rowCount =
        historyDataHelper.countByHistoryDataIdAndAndDataType(
            historicalDataId, dataInfo.getDataType());
    HistoryDataPageModel<HistoricalData> historyDataPageModel =
        PageUtils.dealOverPage(pageIndex, pageSize, rowCount);
    List<HistoricalData> list =
        historyDataRepository.findByHistoryDataIdAndDataType(
            historicalDataId, dataInfo.getDataType(), pageIndex, pageSize);
    historyDataPageModel.setData(list);
    return historyDataPageModel;
  }

  @Override
  public HistoryDataPageModel<HistoricalData> findByHistoryDataIdAndDataTime(
      HistoryDataInfo dataInfo, Integer pageIndex, Integer pageSize) {
    String accountId = dataInfo.getAccountId();
    String productionId = dataInfo.getProductionId();
    String devcieId = dataInfo.getDeviceId();
    if (!this.getPermission(accountId, productionId, devcieId)) {
      throw new PermissionFailException("查询权限有误");
    }
    String historicalDataId = accountId + ":" + productionId + ":" + devcieId;
    long rowCount =
        historyDataHelper.countByHistoryDataIdAndDataTimeBetween(
            historicalDataId, dataInfo.getStartTime(), dataInfo.getEndTime());
    HistoryDataPageModel<HistoricalData> historyDataPageModel =
        PageUtils.dealOverPage(pageIndex, pageSize, rowCount);
    List<HistoricalData> list =
        historyDataRepository.findByHistoryDataIdAndDataTime(
            historicalDataId, dataInfo.getStartTime(), dataInfo.getEndTime(), pageIndex, pageSize);
    historyDataPageModel.setData(list);
    return historyDataPageModel;
  }

  /**
   * 判断是否有查询权限
   *
   * @param accountId
   * @param productionId
   * @param deviceId
   * @return
   */
  private boolean getPermission(String accountId, String productionId, String deviceId) {
    Device device = deviceRepository.findById(accountId, productionId, deviceId);
    if (device != null) {
      return true;
    } else return false;
  }
}
