package com.warpfuture.service;

import com.warpfuture.dto.DeviceLoginDto;
import com.warpfuture.dto.ProtocolRouteDto;
import com.warpfuture.entity.RepayEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/** Created by fido on 2018/4/26. */
@FeignClient(value = "wf-iot-device-service")
public interface DeviceService {

  /**
   * 弱校验
   *
   * @param deviceLoginDto
   * @return
   */
  @RequestMapping(value = "/auth/checkWithKey", method = RequestMethod.POST)
  RepayEntity<Map<String, Object>> checkWithKey(@RequestBody DeviceLoginDto deviceLoginDto);

  /**
   * 强校验
   *
   * @param deviceLoginDto
   * @return
   */
  @RequestMapping(value = "/auth/checkWithSecure", method = RequestMethod.POST)
  RepayEntity<Map<String, Object>> checkWithSecure(@RequestBody DeviceLoginDto deviceLoginDto);

  /**
   * 用户请求鉴权结果
   *
   * @param protocolRouteDto
   * @return
   */
  @RequestMapping(value = "/route/userToDev", method = RequestMethod.POST)
  RepayEntity<Boolean> userToDev(@RequestBody ProtocolRouteDto protocolRouteDto);

  /**
   * 设备请求鉴权结果
   *
   * @param protocolRouteDto
   * @return
   */
  @RequestMapping(value = "/route/devToUser", method = RequestMethod.POST)
  RepayEntity<List<String>> devToUser(@RequestBody ProtocolRouteDto protocolRouteDto);
}
