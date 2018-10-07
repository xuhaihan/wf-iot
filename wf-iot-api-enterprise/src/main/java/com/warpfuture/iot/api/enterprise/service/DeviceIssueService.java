package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.dto.CloudCommunicateMsg;

public interface DeviceIssueService {

    boolean issue(CloudCommunicateMsg cloudCommunicateMsg);
}
