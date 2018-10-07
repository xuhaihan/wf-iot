package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.dto.UploadDeviceInfo;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "wf-iot-production-service")
public interface OTAEnterpriseFeignService {

    @PostMapping(value = "/ota/uploading")
    ResultVO uploadingOTA(@RequestBody UploadDeviceInfo uploadDeviceInfo);
}
