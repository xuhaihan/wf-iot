package com.warpfuture.controller;

import com.warpfuture.dto.ProtocolRouteDto;
import com.warpfuture.entity.RepayEntity;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Created by fido on 2018/4/14. 鉴权接口 */
@RestController
@RequestMapping("/route")
public class RouteController {

  @Autowired private RouteService routeService;

  private Logger logger = LoggerFactory.getLogger(RouteController.class);

  @RequestMapping(value = "/userToDev", method = RequestMethod.POST)
  public RepayEntity<Boolean> userToDev(@RequestBody ProtocolRouteDto protocolRouteDto) {
    logger.info("--用户请求鉴权--：" + protocolRouteDto);
    String productionId = protocolRouteDto.getProductionId();
    String deviceId = protocolRouteDto.getDeviceId();
    String userId = protocolRouteDto.getUserId();
    String applicationId = protocolRouteDto.getApplicationId();
    boolean result = routeService.userToDev(applicationId, productionId, deviceId, userId);
    RepayEntity resultVO = null;
    if (result == false) {
      resultVO = new RepayEntity(ResponseCode.ROUTE_USERTODEV_FAIL.getCode(), "route fail");
    } else {
      resultVO = new RepayEntity(ResponseCode.ROUTE_USERTODEV_SUCCESS.getCode(), "route success");
    }
    resultVO.setData(result);
    return resultVO;
  }

  @RequestMapping(value = "/devToUser", method = RequestMethod.POST)
  public RepayEntity<List<String>> devToUser(@RequestBody ProtocolRouteDto protocolRouteDto) {
    logger.info("---设备的鉴权---：" + protocolRouteDto);
    String productionId = protocolRouteDto.getProductionId();
    String deviceId = protocolRouteDto.getDeviceId();
    RepayEntity resultVO = null;
    List<String> userIdList = routeService.devToUser(productionId, deviceId);
    if (userIdList == null || userIdList.size() == 0) {
      resultVO = new RepayEntity(ResponseCode.ROUTE_DEVTOUSER_FAIL.getCode(), "route fail");
      resultVO.setData(null);
    } else {
      resultVO = new RepayEntity(ResponseCode.ROUTE_DEVTOUSER_SUCCESS.getCode(), "route success");
      resultVO.setData(userIdList);
    }
    return resultVO;
  }
}
