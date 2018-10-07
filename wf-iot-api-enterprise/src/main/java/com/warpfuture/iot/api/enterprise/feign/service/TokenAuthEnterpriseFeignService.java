package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.entity.RepayEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "wf-iot-oauth-service")
public interface TokenAuthEnterpriseFeignService {

    @PostMapping(value = "/oauth/verifyJwt")
    RepayEntity<Map<String, Object>> verifyJwt(@RequestParam(value = "token") String token);

}
