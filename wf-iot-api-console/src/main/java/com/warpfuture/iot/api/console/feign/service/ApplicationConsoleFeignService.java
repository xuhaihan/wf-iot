package com.warpfuture.iot.api.console.feign.service;

import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wf-iot-oauth-service")
public interface ApplicationConsoleFeignService {

    @PostMapping(value = "/application/add")
    ResultVO createApp(@RequestBody ApplicationEntity applicationEntity);

    @PostMapping(value = "/application/update")
    ResultVO updateApp(@RequestBody ApplicationEntity applicationEntity);

    @PostMapping(value = "/application/query")
    ResultVO queryApp(@RequestBody ApplicationEntity applicationEntity);

    @PostMapping(value = "/application/delete")
    ResultVO deleteApp(@RequestParam(value = "applicationId") String applicationId);

    @PostMapping(value = "/application/list")
    ResultVO listApplication(@RequestParam(value = "accountId") String accountId,
                             @RequestParam(value = "pageSize") Integer pageSize,
                             @RequestParam(value = "pageIndex") Integer pageIndex);

    @PostMapping(value = "/application/update/basic")
    ResultVO updateAppBasic(@RequestBody ApplicationEntity applicationEntity);

    @PostMapping(value = "/application/update/expansion")
    ResultVO updateAppExpansion(@RequestBody ApplicationEntity applicationEntity);

    @PostMapping(value = "/application/update/oauthways")
    ResultVO updateAppOAuthWays(@RequestBody ApplicationEntity applicationEntity);

    //    @PostMapping(value = "applicationandproduction")
    //    ResultVO appAndProduct(@RequestParam(value = "applicationId") String applicationId,
    // @RequestParam(value = "productId") String productId);
}
