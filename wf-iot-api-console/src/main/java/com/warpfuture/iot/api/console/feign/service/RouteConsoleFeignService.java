package com.warpfuture.iot.api.console.feign.service;

import com.warpfuture.dto.RouteInfoDto;
import com.warpfuture.entity.Route;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "wf-iot-production-service")
public interface RouteConsoleFeignService {

    @PostMapping(value = "/route/create")
    ResultVO<List<Route>> createRoute(@RequestBody RouteInfoDto routeInfoDto);

    @PostMapping(value = "/route/query")
    ResultVO<List<Route>> queryRoute(@RequestBody RouteInfoDto routeInfoDto);

}
