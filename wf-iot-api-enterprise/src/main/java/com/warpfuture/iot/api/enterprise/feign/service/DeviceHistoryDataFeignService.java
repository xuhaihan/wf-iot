package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.entity.HistoricalData;
import com.warpfuture.entity.HistoryDataPageModel;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-device-service")
public interface DeviceHistoryDataFeignService {

    @PostMapping(value = "/historyData/findByHistoryDataId")
    ResultVO<HistoryDataPageModel<HistoricalData>> findHistoryDataByDeviceId(@RequestBody HistoryDataInfo dataInfo,
                                                                             @RequestParam Integer pageSize,
                                                                             @RequestParam Integer pageIndex);

    @PostMapping(value = "/historyData/findByHistoryDataIdAndDataType")
    ResultVO<HistoryDataPageModel<HistoricalData>> findHistoryDataByDeviceIdAndDeviceType(
            @RequestBody HistoryDataInfo dataInfo,
            @RequestParam Integer pageSize,
            @RequestParam Integer pageIndex);

    @PostMapping(value = "/historyData/findByHistoryDataIdAndDataTime")
    ResultVO<HistoryDataPageModel<HistoricalData>> findByHistoryDataIdAndDataTime(
            @RequestBody HistoryDataInfo dataInfo,
            @RequestParam Integer pageSize,
            @RequestParam Integer pageIndex);
}
