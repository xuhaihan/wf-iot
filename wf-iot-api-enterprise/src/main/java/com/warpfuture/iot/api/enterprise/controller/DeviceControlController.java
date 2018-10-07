package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.DeviceControlFront;
import com.warpfuture.iot.api.enterprise.service.DeviceControlService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DeviceControlController {

    @Autowired
    private DeviceControlService deviceControlService;

    /**
     * 断开设备连接
     *
     * @param deviceControlFront
     * @return
     */
    @PostMapping(value = "/control")
    public ResultVO deviceControl(@RequestBody DeviceControlFront deviceControlFront, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId)) {
            if (deviceControlFront != null &&
                    deviceControlFront.getDevList() != null &&
                    !deviceControlFront.getDevList().isEmpty()) {
                deviceControlFront.setAccountId(accountId);
                boolean res = deviceControlService.control(deviceControlFront);
                return res ? new ResultVO().success() : new ResultVO().fail(ResponseMsg.REQUEST_ERROR);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

}
