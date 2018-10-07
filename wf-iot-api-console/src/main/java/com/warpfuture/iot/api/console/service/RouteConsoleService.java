package com.warpfuture.iot.api.console.service;

import com.warpfuture.dto.RouteInfoDto;
import com.warpfuture.entity.Route;
import com.warpfuture.vo.ResultVO;

import java.util.List;

public interface RouteConsoleService {

    ResultVO createRoute(RouteInfoDto routeInfoDto);

    ResultVO queryRoute(RouteInfoDto routeInfoDto);

}
