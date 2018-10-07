package com.warpfuture.iot.api.console.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.RouteInfoDto;
import com.warpfuture.entity.Route;
import com.warpfuture.iot.api.console.feign.service.RouteConsoleFeignService;
import com.warpfuture.iot.api.console.service.RouteConsoleService;
import com.warpfuture.iot.api.console.util.BeanUtil;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RouteConsoleServiceImpl implements RouteConsoleService {

    @Autowired
    private RouteConsoleFeignService routeConsoleFeignService;

    @Override
    public ResultVO createRoute(RouteInfoDto routeInfoDto) {
        ResultVO routeResult;
        try {
            routeResult = BeanUtil.getRouteResult(routeConsoleFeignService.createRoute(routeInfoDto));
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return routeResult == null ? new ResultVO<List<Route>>().fail(ResponseMsg.REQUEST_ERROR) : routeResult;
    }

    @Override
    public ResultVO queryRoute(RouteInfoDto routeInfoDto) {
        ResultVO routeResult;
        try {
            routeResult = BeanUtil.getRouteResult(routeConsoleFeignService.queryRoute(routeInfoDto));
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return routeResult == null ? new ResultVO<List<Route>>().fail(ResponseMsg.REQUEST_ERROR) : routeResult;
    }
}
