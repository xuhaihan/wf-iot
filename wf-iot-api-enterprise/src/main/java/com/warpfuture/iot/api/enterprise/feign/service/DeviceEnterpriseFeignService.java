package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.entity.Device;
import com.warpfuture.entity.Production;
import com.warpfuture.entity.RouteInfo;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "wf-iot-production-service")
public interface DeviceEnterpriseFeignService {

    @PostMapping(value = "/deviceDetails/getOnlineDevsByProd")
    ResultVO getOnlineDeviceByProductionId(@RequestBody Production production,
                                           @RequestParam(value = "pageSize") Integer pageSize,
                                           @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/deviceDetails/getOffLineDevsByProd")
    ResultVO getOfflineDeviceByProductionId(@RequestBody Production production,
                                            @RequestParam(value = "pageSize") Integer pageSize,
                                            @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/deviceDetails/getAllDevsByProd")
    ResultVO getAllDeviceByProductionId(@RequestBody Production production,
                                        @RequestParam(value = "pageSize") Integer pageSize,
                                        @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/deviceDetails/getOnlineDevsByAccount")
    ResultVO getOnlineDeviceByAccountId(@RequestParam(value = "accountId") String accountId,
                                        @RequestParam(value = "pageSize") Integer pageSize,
                                        @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/deviceDetails/getOffLineDevsByAccount")
    ResultVO getOfflineDeviceByAccountId(@RequestParam(value = "accountId") String accountId,
                                         @RequestParam(value = "pageSize") Integer pageSize,
                                         @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/deviceDetails/getAllDevsByAccount")
    ResultVO getAllDeviceByAccountId(@RequestParam(value = "accountId") String accountId,
                                     @RequestParam(value = "pageSize") Integer pageSize,
                                     @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/deviceDetails/getDeviceByDeviceId")
    ResultVO<Device> getDeviceByDeviceId(@RequestBody Device device);

    @PostMapping(value = "/deviceDetails/getAuthRecord")
    ResultVO<List<RouteInfo>> getAuthRecord(@RequestBody Device device);

}
