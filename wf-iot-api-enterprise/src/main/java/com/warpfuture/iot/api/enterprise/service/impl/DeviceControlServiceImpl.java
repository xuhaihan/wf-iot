package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.Command;
import com.warpfuture.dto.DeviceControl;
import com.warpfuture.dto.DeviceControlFront;
import com.warpfuture.iot.api.enterprise.service.DeviceControlService;
import com.warpfuture.iot.api.enterprise.stream.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceControlServiceImpl implements DeviceControlService {

    @Autowired
    private Sender sender;

    /**
     * 断开设备连接
     *
     * @param deviceControlFront
     * @return
     */
    @Override
    public boolean control(DeviceControlFront deviceControlFront) {
        return sender.send(new DeviceControl(
                Command.ENTERPRISE_KILL_DEVICES,
                System.currentTimeMillis(),
                deviceControlFront.getAccountId(),
                deviceControlFront.getDevList(),
                deviceControlFront.getContent()));
    }

}
