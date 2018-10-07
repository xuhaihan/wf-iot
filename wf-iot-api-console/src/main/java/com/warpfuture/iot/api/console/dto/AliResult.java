package com.warpfuture.iot.api.console.dto;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Data
@ToString
public class AliResult {

    private String url;

    /**
     * objectName 阿里云的对象名
     */
    private String key;

    /**
     * 文件hash
     */
    private String md5;

    public AliResult() {
    }

    public AliResult(String url, String key,String md5) {
        this.key = key;
        this.url = url;
        this.md5 = md5;
    }

    public boolean isSuccess() {
        return !StringUtils.isEmpty(this.md5) && !StringUtils.isEmpty(this.url);
    }
}
