package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.Production;
import com.warpfuture.entity.RouteInfo;
import com.warpfuture.vo.ResultVO;

import java.util.List;

public interface DeviceEnterpriseService {

    ResultVO getOnlineDeviceByProductionId(Production production, Integer pageSize, Integer pageIndex);

    ResultVO getOfflineDeviceByProductionId(Production production, Integer pageSize, Integer pageIndex);

    ResultVO getAllDeviceByProductionId(Production production, Integer pageSize, Integer pageIndex);

    ResultVO getOnlineDeviceByAccountId(String accountId, Integer pageSize, Integer pageIndex);

    ResultVO getOfflineDeviceByAccountId(String accountId, Integer pageSize, Integer pageIndex);

    ResultVO getAllDeviceByAccountId(String accountId, Integer pageSize, Integer pageIndex);

    ResultVO findHistoryDataByDeviceId(HistoryDataInfo dataInfo, Integer pageSize, Integer pageIndex);

    ResultVO findHistoryDataByDeviceIdAndDeviceType(HistoryDataInfo dataInfo, Integer pageSize, Integer pageIndex);

    ResultVO findByHistoryDataIdAndDataTime(HistoryDataInfo dataInfo, Integer pageSize, Integer pageIndex);

    ResultVO getDeviceByDeviceId(Device device);

    ResultVO getAuthRecord(Device device);

}
