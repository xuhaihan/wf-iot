package com.warpfuture.controller;

import com.warpfuture.dto.DeviceLoginDto;
import com.warpfuture.entity.RepayEntity;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.service.LoginAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Created by fido on 2018/4/13. 设备登录连接认证接口 */
@RestController
@RequestMapping("/auth")
public class LoginAuthController {

  @Autowired private LoginAuthService loginAuthService;
  private Logger logger = LoggerFactory.getLogger(LoginAuthController.class);

  @RequestMapping(value = "/checkWithKey", method = RequestMethod.POST)
  public RepayEntity<Map<String, Object>> checkWithKey(@RequestBody DeviceLoginDto deviceLoginDto) {
    logger.info("--设备请求登录验证的数据:--" + deviceLoginDto);
    String productionKey = deviceLoginDto.getProductionKey();
    String deviceId = deviceLoginDto.getDeviceId();
    RepayEntity resultVO = null;
    Map<String, Object> result = loginAuthService.checkWithKey(productionKey, deviceId);
    logger.info("--设备请求登录认证的结果：--" + result);
    if (result == null) {
      resultVO = new RepayEntity(ResponseCode.LOGIN_AUTH_FAIL.getCode(), "device login fail");
    } else {
      resultVO =
          new RepayEntity(
              ResponseCode.LOGIN_AUTH_SUCCESS.getCode(), "device login success", result);
    }
    return resultVO;
  }

  @RequestMapping(value = "/checkWithSecure", method = RequestMethod.POST)
  public RepayEntity<Map<String, Object>> checkWithSecure(
      @RequestBody DeviceLoginDto deviceLoginDto) {
    logger.info("--设备请求强校验登录验证的数据:--" + deviceLoginDto);
    String pksToken = deviceLoginDto.getPksToken();
    String mode = deviceLoginDto.getMode();
    RepayEntity resultVO = null;
    Map<String, Object> result = loginAuthService.checkWithSecure(pksToken, mode);
    logger.info("--设备请求强校验登录认证的结果：--" + result);
    if (result == null) {
      resultVO = new RepayEntity(ResponseCode.LOGIN_AUTH_FAIL.getCode(), "device login fail");
    } else {
      resultVO =
          new RepayEntity(
              ResponseCode.LOGIN_AUTH_SUCCESS.getCode(), "device login success", result);
    }
    return resultVO;
  }
}
