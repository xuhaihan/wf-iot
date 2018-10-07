package com.warpfuture.iot.api.console.service;

import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.vo.ResultVO;

public interface DeviceConsoleService {

    ResultVO getDashBoardInfo(String accountId);

    ResultVO getDeviceData(String accountId);

    ResultVO getAllDeviceByProductionId(Production production, PageModel pageModel);

    ResultVO getDeviceByDeviceId(Device device);

    ResultVO findHistoryDataByDeviceId(HistoryDataInfo dataInfo,PageModel pageModel);
}
