package com.warpfuture.controller;

import com.warpfuture.constant.Constant;
import com.warpfuture.dto.RouteInfoDto;
import com.warpfuture.entity.Route;
import com.warpfuture.exception.ParameterIllegalException;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.RouteService;
import com.warpfuture.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Created by fido on 2018/4/26. */
@RestController
@RequestMapping("/route")
public class RouteController {
  @Autowired private RouteService routeService;
  private Logger logger = LoggerFactory.getLogger(RouteController.class);

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public ResultVO<List<Route>> createRoute(@RequestBody RouteInfoDto routeInfoDto) {
    ResultVO<List<Route>> resultVO = null;
    try {
      String accountId = routeInfoDto.getAccountId();
      String applicationId = routeInfoDto.getApplicationId();
      List<Route> productions = routeInfoDto.getProductions();
      List<Route> list =
          routeService.createRouteByApplicationId(accountId, applicationId, productions);
      resultVO = new ResultVO<List<Route>>(Constant.SUCCESS, "创建路由配置成功", list);
    } catch (ParameterIllegalException e) {
      logger.debug(e.getMessage());
      resultVO = new ResultVO<>(Constant.FAIL, e.getMessage());
    } catch (PermissionFailException e) {
      logger.debug(e.getMessage());
      resultVO = new ResultVO<>(Constant.FAIL, e.getMessage());
    } catch (Exception e) {
      logger.debug("创建路由配置异常");
      e.printStackTrace();
      resultVO = new ResultVO<>(Constant.FAIL, "创建路由配置失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/query", method = RequestMethod.POST)
  public ResultVO<List<Route>> updateRoute(@RequestBody Route route) {
    ResultVO<List<Route>> resultVO = null;
    try {
      String accountId = route.getAccountId();
      String applicationId = route.getApplicationId();
      List<Route> list = routeService.findByApplicationId(accountId, applicationId);
      resultVO = new ResultVO<List<Route>>(Constant.SUCCESS, "查找路由配置成功", list);
    } catch (PermissionFailException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(Constant.FAIL, e.getMessage());
    } catch (ParameterIllegalException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(Constant.FAIL, e.getMessage());
    } catch (Exception e) {
      logger.debug("查询路由配置异常");
      resultVO = new ResultVO<>(Constant.FAIL, "查询路由配置失败");
    }
    return resultVO;
  }
}
