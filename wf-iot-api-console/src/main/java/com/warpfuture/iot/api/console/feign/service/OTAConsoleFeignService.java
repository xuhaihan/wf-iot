package com.warpfuture.iot.api.console.feign.service;

import com.warpfuture.entity.OTAInfo;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-production-service")
public interface OTAConsoleFeignService {

    @PostMapping(value = "/ota/create")
    ResultVO createOTA(@RequestBody OTAInfo otaInfo);

    @PostMapping(value = "/ota/query")
    ResultVO queryOTA(@RequestBody OTAInfo otaInfo,
                      @RequestParam(value = "pageSize") Integer pageSize,
                      @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/ota/disable")
    ResultVO disableOTA(@RequestBody OTAInfo otaInfo);


}
