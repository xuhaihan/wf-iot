package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.dto.ConnectAutoDto;
import com.warpfuture.entity.RouteInfo;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "wf-iot-device-service")
public interface ConnectEnterpriseFeignService {

    @PostMapping(value = "/connection/requestConnect")
    ResultVO requestConnect(ConnectAutoDto connectAutoDto);

    @PostMapping(value = "/connection/removeConnect")
    ResultVO removeConnect(ConnectAutoDto connectAutoDto);

}
