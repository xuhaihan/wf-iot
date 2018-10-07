package com.warpfuture.dto;

import lombok.Data;

import java.util.List;

/**
 * 断开设备时用来接收前端的字段的实体
 */
@Data
public class DeviceControlFront {

    private String content;

    private String accountId;

    private List<String> devList;
}
