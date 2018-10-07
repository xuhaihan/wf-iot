package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.UploadDeviceInfo;
import com.warpfuture.iot.api.enterprise.feign.service.OTAEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.OTAEnterpriseService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OTAEnterpriseServiceImpl implements OTAEnterpriseService {

    @Autowired
    private OTAEnterpriseFeignService otaEnterpriseFeignService;

    @Override
    public ResultVO uploadingOTA(UploadDeviceInfo uploadDeviceInfo) {
        ResultVO result;
        try {
            result = otaEnterpriseFeignService.uploadingOTA(uploadDeviceInfo);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
