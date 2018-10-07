package com.warpfuture.iot.api.console.feign.service;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.Production;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-production-service")
public interface DeviceInfoConsoleFeignService {

    @PostMapping(value = "/deviceDetails/getAllDevsByProd")
    ResultVO getAllDeviceByProductionId(@RequestBody Production production,
                                        @RequestParam(value = "pageIndex") Integer pageIndex,
                                        @RequestParam(value = "pageSize") Integer pageSize);

    @PostMapping(value = "/deviceDetails/getDeviceByDeviceId")
    ResultVO<Device> getDeviceByDeviceId(@RequestBody Device device);
}
