package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.UploadDeviceInfo;
import com.warpfuture.iot.api.enterprise.service.OTAEnterpriseService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OTAEnterpriseController {

    @Autowired
    private OTAEnterpriseService otaEnterpriseService;

    @PostMapping(value = "/ota/uploading")
    public ResultVO uploadingOTA(@RequestBody UploadDeviceInfo uploadDeviceInfo) {
        if (CompareUtil.uploadDeviceInfoNotNull(uploadDeviceInfo)) {
            return otaEnterpriseService.uploadingOTA(uploadDeviceInfo);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }
}
