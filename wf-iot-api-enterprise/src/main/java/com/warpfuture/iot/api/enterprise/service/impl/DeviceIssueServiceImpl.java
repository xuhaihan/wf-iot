package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.Command;
import com.warpfuture.dto.CloudCommunicateMsg;
import com.warpfuture.iot.api.enterprise.service.DeviceIssueService;
import com.warpfuture.iot.api.enterprise.stream.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceIssueServiceImpl implements DeviceIssueService {

    @Autowired
    private Sender sender;

    /**
     * 向设备发送数据
     *
     * @param cloudCommunicateMsg
     * @return
     */
    @Override
    public boolean issue(CloudCommunicateMsg cloudCommunicateMsg) {
        cloudCommunicateMsg.setMessageTime(System.currentTimeMillis());
        cloudCommunicateMsg.setData(cloudCommunicateMsg.getData() + "\n");
        CloudCommunicateMsg.Source source = cloudCommunicateMsg.getSource();
        if (source != null) {
            source.setFlag(Command.ENTERPRISE_TAG);
        } else {
            source = CloudCommunicateMsg.getSourceInstance();
            source.setFlag(Command.ENTERPRISE_TAG);
        }
        cloudCommunicateMsg.setSource(source);
        return sender.send(cloudCommunicateMsg);
    }
}
