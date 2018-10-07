package com.warpfuture.iot.api.console.feign.service;

import com.warpfuture.dto.HistoryDataInfo;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-device-service")
public interface DeviceConsoleFeignService {

    @PostMapping(value = "/dashboard/getDashBoardInfo")
    ResultVO getDashBoardInfo(@RequestParam(value = "accountId") String accountId);

    @PostMapping(value = "/deviceData/getDeviceData")
    ResultVO getDeviceData(@RequestParam(value = "accountId") String accountId);

    @PostMapping(value = "/historyData/findByHistoryDataId")
    ResultVO findHistoryDataByDeviceId(@RequestBody HistoryDataInfo dataInfo,
                                       @RequestParam Integer pageSize,
                                       @RequestParam Integer pageIndex);

    @PostMapping(value = "/historyData/findByHistoryDataIdAndDataType")
    ResultVO findHistoryDataByDeviceIdAndDeviceType(
            @RequestBody HistoryDataInfo dataInfo,
            @RequestParam Integer pageSize,
            @RequestParam Integer pageIndex);

    @PostMapping(value = "/historyData/findByHistoryDataIdAndDataTime")
    ResultVO findByHistoryDataIdAndDataTime(
            @RequestBody HistoryDataInfo dataInfo,
            @RequestParam Integer pageSize,
            @RequestParam Integer pageIndex);
}
