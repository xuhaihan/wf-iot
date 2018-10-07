package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-production-service")
public interface ProductEnterpriseFeignService {

    @PostMapping(value = "/production/account/query")
    ResultVO queryProductionByAccountId(@RequestParam(value = "accountId") String accountId,
                                        @RequestParam(value = "pageSize") Integer pageSize,
                                        @RequestParam(value = "pageIndex") Integer pageIndex);

}
