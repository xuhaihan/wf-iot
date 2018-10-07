package com.warpfuture.controller;

import com.warpfuture.dto.DeviceData;
import com.warpfuture.entity.Device;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.service.DeviceDataService;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

/** Created by fido on 2018/5/6. */
@RestController
@RequestMapping("/deviceData")
public class DeviceDataController {
  @Autowired private DeviceDataService deviceDataService;

  @RequestMapping(value = "/getDeviceData", method = RequestMethod.POST)
  public ResultVO<DeviceData> getDeviceData(@RequestParam(value = "accountId") String accountId) {
    long current = System.currentTimeMillis(); // 当前时间毫秒数
    long zero =
        current / (1000 * 3600 * 24) * (1000 * 3600 * 24)
            - TimeZone.getDefault().getRawOffset(); // 今天零点零分零秒的毫秒数
    DeviceData deviceData = deviceDataService.getDeviceData(accountId, zero);
    return new ResultVO<>(ResponseCode.OPERATION_SUCCESS.getCode(), "获取成功", deviceData);
  }

  @RequestMapping(value = "/updateExtensions", method = RequestMethod.POST)
  public ResultVO<Device> updateExtensions(@RequestBody Device device) {
    ResultVO<Device> resultVO = null;
    Device findDevice = deviceDataService.updateExtensions(device);
    if (findDevice != null) {
      resultVO = new ResultVO<>(ResponseCode.OPERATION_SUCCESS.getCode(), "更新设备属性成功", findDevice);
    } else resultVO = new ResultVO<>(ResponseCode.OPERATION_FAIL.getCode(), "更新设备属性失败");
    return resultVO;
  }
}
