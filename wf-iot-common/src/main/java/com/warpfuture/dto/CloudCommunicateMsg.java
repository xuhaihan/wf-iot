package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 向设备发送数据的实体
 */
@Data
@ToString
public class CloudCommunicateMsg {

    private String accountId;

    private String data;

    private Long messageTime;

    private Source source;

    private Target target;

    public static Source getSourceInstance() {
        return new Source();
    }

    @Data
    @ToString
    public static class Source {

        private String flag;

        private String applicationId;

        private String sessionId;

        private String deviceId;

        private String userId;
    }

    @Data
    @ToString
    public class Target {
        private String productionId;

        private String deviceId;
    }

}
