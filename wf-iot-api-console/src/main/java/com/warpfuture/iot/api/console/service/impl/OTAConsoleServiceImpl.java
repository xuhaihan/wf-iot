package com.warpfuture.iot.api.console.service.impl;

import com.warpfuture.constant.AliProperty;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.OTAInfo;
import com.warpfuture.entity.PageModel;
import com.warpfuture.iot.api.console.dto.AliResult;
import com.warpfuture.iot.api.console.exception.SendAliOssException;
import com.warpfuture.iot.api.console.feign.service.OTAConsoleFeignService;
import com.warpfuture.iot.api.console.service.OTAConsoleService;
import com.warpfuture.iot.api.console.util.AliOssUtil;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class OTAConsoleServiceImpl implements OTAConsoleService {

    @Autowired
    private OTAConsoleFeignService otaConsoleFeignService;

    @Override
    public ResultVO createOTA(OTAInfo otaInfo) {
        MultipartFile file = otaInfo.getFile();
        AliResult aliResult = null;
        try {
            if (file != null) {
                aliResult = AliOssUtil.putObject(file, otaInfo.getOtaName() + "-" + otaInfo.getAccountId() + "-" + otaInfo.getProductionId(), AliProperty.otaFileKey);
            }
        } catch (SendAliOssException e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(e.getMessage());
        }
        if (aliResult != null && aliResult.isSuccess()) {
            otaInfo.setOtaFileSize(file.getSize());
            otaInfo.setOtaFileURL(aliResult.getUrl());
            otaInfo.setOtaHash(aliResult.getMd5());
            otaInfo.setFile(null);
            ResultVO result;
            try {
                result = otaConsoleFeignService.createOTA(otaInfo);
            } catch (Exception e) {
                log.warn("Service Error : {}", e.getMessage());
                return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
            }
            return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
        }
        return new ResultVO().fail((ResponseMsg.OTA_UPLOAD_ERROR));
    }

    @Override
    public ResultVO queryOTA(OTAInfo otaInfo, PageModel pageModel) {
        ResultVO result;
        try {
            result = otaConsoleFeignService.queryOTA(otaInfo,pageModel.getPageSize(),pageModel.getPageIndex());
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO disableOTA(OTAInfo otaInfo) {
        ResultVO result;
        try {
            result = otaConsoleFeignService.disableOTA(otaInfo);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO uploadingOTA(OTAInfo otaInfo) {
        return null;
    }
}
