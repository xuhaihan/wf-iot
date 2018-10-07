package com.warpfuture.controller;

import com.warpfuture.dto.ConnectAutoDto;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.ConnectAuthService;
import com.warpfuture.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** Created by fido on 2018/4/14. 建立用户与设备的权限 提供给企业API */
@RestController
@RequestMapping("/connection")
public class ConnectAuthController {

  @Autowired private ConnectAuthService connectAuthService;
  private Logger log = LoggerFactory.getLogger(ConnectAuthController.class);

  @RequestMapping(value = "/requestConnect", method = RequestMethod.POST)
  public ResultVO requestConnect(@RequestBody ConnectAutoDto connectAutoDto) {
    log.info("---配置权限的请求：---" + connectAutoDto);
    String accountId = connectAutoDto.getAccountId();
    String productionId = connectAutoDto.getProductionId();
    String deviceId = connectAutoDto.getDeviceId();
    String userId = connectAutoDto.getUserId();
    String applicationId = connectAutoDto.getApplicationId();
    Long keepTime = connectAutoDto.getKeepTime();
    Long startTime = connectAutoDto.getStartTime();
    ResultVO resultVO = null;
    boolean result = false;
    try {
      result =
          connectAuthService.addConnectAuth(
              accountId, productionId, deviceId, userId, applicationId, keepTime, startTime);
      log.info("---配置权限的结果：---" + result);
      if (!result) {
        resultVO =
            new ResultVO(ResponseCode.REQUEST_CONNECT_FAIL.getCode(), "request connection fail");
      } else {
        resultVO =
            new ResultVO(
                ResponseCode.REQUEST_CONNECT_SUCCESS.getCode(), "request connection success");
      }
    } catch (PermissionFailException e) {
      log.info("配置权限有误");
      resultVO = new ResultVO(ResponseCode.REQUEST_CONNECT_FAIL.getCode(), e.getMessage());
    }
    return resultVO;
  }

  @RequestMapping(value = "/removeConnect", method = RequestMethod.POST)
  public ResultVO removeConnect(@RequestBody ConnectAutoDto connectAutoDto) {
    log.info("---移除权限的请求：---" + connectAutoDto);
    String accountId = connectAutoDto.getAccountId();
    String productionId = connectAutoDto.getProductionId();
    String deviceId = connectAutoDto.getDeviceId();
    String userId = connectAutoDto.getUserId();
    String applicationId = connectAutoDto.getApplicationId();
    ResultVO resultVO = null;
    boolean result = false;
    try {
      result =
          connectAuthService.removeConnectAuth(
              accountId, productionId, deviceId, userId, applicationId);
      log.info("---移除权限的结果：---" + result);
      if (!result) {
        resultVO =
            new ResultVO(ResponseCode.REMOVE_CONNECT_FAIL.getCode(), "remove connection fail");
      } else {
        resultVO =
            new ResultVO(
                ResponseCode.REMOVE_CONNECT_SUCCESS.getCode(), "remove connection success");
      }
    } catch (PermissionFailException e) {
      log.info("移除权限有误");
      resultVO = new ResultVO(ResponseCode.REMOVE_CONNECT_FAIL.getCode(), e.getMessage());
    }
    return resultVO;
  }
}
