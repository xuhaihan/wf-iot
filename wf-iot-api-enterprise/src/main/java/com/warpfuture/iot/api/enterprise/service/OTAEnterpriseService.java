package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.dto.UploadDeviceInfo;
import com.warpfuture.vo.ResultVO;

public interface OTAEnterpriseService {

    ResultVO uploadingOTA(UploadDeviceInfo uploadDeviceInfo);
}
