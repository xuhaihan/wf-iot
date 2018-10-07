package com.warpfuture.iot.api.console.feign.service;

import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@FeignClient(value = "wf-iot-oauth-service")
public interface OAuthConsoleFeignService {

    @PostMapping(value = "/oauth/woaUrl")
    ResultVO getWOAUrl(@NotNull @RequestParam(value = "applicationId") String applicationId,
                       @NotNull @RequestParam(value = "accountId") String accountId);

}
