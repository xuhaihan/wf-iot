package com.warpfuture.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.warpfuture.constant.Constant;
import com.warpfuture.entity.*;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.DeviceInfoRepository;
import com.warpfuture.repository.HistoryDataRepository;
import com.warpfuture.repository.OTARepository;
import com.warpfuture.repository.ProductionRepository;
import com.warpfuture.service.OTAService;
import com.warpfuture.util.IdUtils;
import com.warpfuture.vo.OTAUpdateInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/** Created by fido on 2018/4/26. */
@Service
public class OTAServiceImpl implements OTAService {
  @Autowired private OTARepository otaRepository;
  @Autowired private ProductionRepository productionRepository;
  @Autowired private DeviceInfoRepository deviceInfoRepository;
  @Autowired private HistoryDataRepository historyDataRepository;
  private Logger logger = LoggerFactory.getLogger(OTAServiceImpl.class);

  @Override
  public OTAInfo createOTA(OTAInfo otaInfo) {
    String accountId = otaInfo.getAccountId();
    String productionId = otaInfo.getProductionId();
    boolean permission = this.getOperatePermission(accountId, productionId);
    if (!permission) {
      throw new PermissionFailException("创建ota权限错误");
    }
    if (otaInfo.getPublishTime() == null) {
      otaInfo.setPublishTime(System.currentTimeMillis()); // 设置发布时间
    }
    String otaId = IdUtils.getId();
    otaInfo.setOtaId(otaId);
    otaInfo.setCreateTime(System.currentTimeMillis()); // 设置ota创建时间
    otaInfo.setUpdateTime(System.currentTimeMillis()); // 设置ota更改时间
    otaInfo.setOtaStatus(Constant.OTA_ALIVE); // 设置OTA状态
    otaRepository.createOTA(otaInfo);
    return otaRepository.queryByOTAId(otaId);
  }

  @Override
  public PageModel<OTAInfo> queryByProductionId(
      String accountId, String productionId, Integer pageSize, Integer pageIndex) {
    if (!getOperatePermission(accountId, productionId)) {
      throw new PermissionFailException("查询ota列表权限错误");
    }
    return otaRepository.queryByProductionId(productionId, pageSize, pageIndex);
  }

  @Override
  public OTAInfo disable(String accountId, String productionId, String otaId) {
    if (!getOperatePermission(accountId, productionId)) {
      throw new PermissionFailException("禁用ota权限错误");
    }
    OTAInfo otaInfo = otaRepository.queryByOTAId(otaId);
    if (!otaInfo.getProductionId().equals(productionId)) {
      throw new PermissionFailException("禁用ota权限错误");
    }
    otaRepository.diable(otaInfo.getOtaId());
    return otaRepository.queryByOTAId(otaId);
  }

  /**
   * 判断是否有操作权限
   *
   * @param accountId
   * @param productionId
   * @return
   */
  private boolean getOperatePermission(String accountId, String productionId) {
    Production production = productionRepository.findByProductionId(productionId);
    if (production == null || !(production.getAccountId().equals(accountId))) {
      return false;
    }
    return true;
  }

  @Override
  public OTAUpdateInfo uploading(
      String productionKey,
      String originOtaVersion,
      String deviceId,
      Map<String, Object> extensions) {

    logger.info(
        "==设备请求OTA固件升级时上报的信息=="
            + productionKey
            + ","
            + originOtaVersion
            + ","
            + deviceId
            + ","
            + extensions);
    long startTime = System.currentTimeMillis();
    Production production = productionRepository.findByProductionKey(productionKey);
    if (production == null) {
      return null;
    }
    deviceInfoRepository.updateOTAInfo(
        production.getAccountId(), production.getProductionId(), deviceId, originOtaVersion);
    Device device =
        deviceInfoRepository.findByDeviceId(
            production.getAccountId(), production.getProductionId(), deviceId);
    if (device == null) {
      return null;
    }
    // 找到产品
    String productionId = device.getProductionId();
    String accountId = device.getAccountId();
    // 设备上报固件版本信息，加入到cassandra数据库中
    String deviceInfo = accountId + ":" + productionId + ":" + deviceId;
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("originOtaVersion", originOtaVersion);
    jsonObject.put("extensions", extensions);
    String dataContent = jsonObject.toJSONString();
    HistoricalData historicalData = new HistoricalData();
    historicalData.setDataType(1); // 设备上报
    historicalData.setHistoryDataId(deviceInfo);
    historicalData.setDataTime(new Date(System.currentTimeMillis()));
    historicalData.setDataContent(dataContent);
    historyDataRepository.insert(historicalData);
    // 获得该产品下最新的ota信息
    OTAInfo otaInfo = otaRepository.getTheLatestOta(productionId);
    if (otaInfo == null) {
      return null;
    }
    // 被禁用了
    if (otaInfo.getOtaStatus() == 1) {
      return null;
    }
    // 存在ota升级信息
    String newVersion = otaInfo.getOtaVersion();
    // 发布时间
    Long publishTime = otaInfo.getPublishTime();
    // 未到发布时间
    if (System.currentTimeMillis() < publishTime) {
      return null;
    }
    // 版本相同，无需升级
    if (newVersion.equals(originOtaVersion)) {
      return null;
    }

    List<String> deviceIdList = otaInfo.getDeviceIdList();
    // 对该产品下所有设备都有效
    if (deviceIdList == null) {
      OTAUpdateInfo otaUpdateInfo = this.getOTAUpdateInfo(otaInfo);
      long deviceEndTime = System.currentTimeMillis();
      logger.info("==判断是否需要OTA升级（针对产品）:==" + (deviceEndTime - startTime) + "ms");
      return otaUpdateInfo;
    } else if (deviceIdList != null) {
      // 是否存在这个设备的特定升级记录
      if (deviceIdList.contains(deviceId)) {
        OTAUpdateInfo otaUpdateInfo = this.getOTAUpdateInfo(otaInfo);
        long proEndTime = System.currentTimeMillis();
        logger.info("==判断是否需要OTA升级（针对设备）:==" + (proEndTime - startTime) + "ms");
        return otaUpdateInfo;
      } else return null;
    }
    return null;
  }

  /**
   * 提炼相关的ota升级信息给设备
   *
   * @param otaInfo
   * @return
   */
  private OTAUpdateInfo getOTAUpdateInfo(OTAInfo otaInfo) {
    OTAUpdateInfo otaUpdateInfo = new OTAUpdateInfo();
    otaUpdateInfo.setProductionId(otaInfo.getProductionId());
    otaUpdateInfo.setOotaFileURL(otaInfo.getOtaFileURL());
    otaUpdateInfo.setOtaDesc(otaInfo.getOtaDesc());
    otaUpdateInfo.setOtaHash(otaInfo.getOtaHash());
    otaUpdateInfo.setOtaVersion(otaInfo.getOtaVersion());
    otaUpdateInfo.setOtaFileSize(otaInfo.getOtaFileSize());
    return otaUpdateInfo;
  }
}
