package com.warpfuture.iot.api.console.feign.service;

import com.warpfuture.entity.Production;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-production-service")
public interface ProductConsoleFeignService {

    @PostMapping(value = "/production/create")
    ResultVO createProduction(@RequestBody Production production);

    @PostMapping(value = "/production/update")
    ResultVO updateProduction(@RequestBody Production production);

    @PostMapping(value = "/production/delete")
    ResultVO deleteProduction(@RequestBody Production production);

    @PostMapping(value = "/production/query")
    ResultVO queryProduction(@RequestBody Production production);

    @PostMapping(value = "/production/account/query")
    ResultVO queryProductionByAccountId(@RequestParam(value = "accountId") String accountId,
                                        @RequestParam(value = "pageSize") Integer pageSize,
                                        @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/production/down")
    ResultVO revoke(@RequestBody Production production);

    @PostMapping(value = "/production/publish")
    ResultVO publish(@RequestBody Production production);

    @PostMapping(value = "/production/updateExtensions")
    ResultVO updateExtensions(@RequestBody Production production);

//    @RequestMapping(value = "production/regenKeys", method = RequestMethod.POST)
//    ResultVO regenKeys(@RequestParam(value = "productId") String productId);
}
