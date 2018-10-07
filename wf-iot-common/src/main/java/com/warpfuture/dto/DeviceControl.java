package com.warpfuture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 断开设备连接的实体
 */
@Data
@AllArgsConstructor
@ToString
public class DeviceControl {

    private String statement;

    private Long messageTime;

    private Map<String, Object> data;

    public DeviceControl(String statement, Long messageTime, String accountId, List<String> devList,String content) {
        this.statement = statement;
        this.messageTime = messageTime;
        data = new HashMap<>();
        data.put("accountId", accountId);
        data.put("objectList", devList);
        data.put("content",content);
    }
}
