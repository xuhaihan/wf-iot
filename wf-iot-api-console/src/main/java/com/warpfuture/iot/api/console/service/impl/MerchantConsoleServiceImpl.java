package com.warpfuture.iot.api.console.service.impl;

import com.warpfuture.constant.AliProperty;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.merchant.WxpayData;
import com.warpfuture.iot.api.console.dto.AliResult;
import com.warpfuture.iot.api.console.exception.SendAliOssException;
import com.warpfuture.iot.api.console.feign.service.MerchantConsoleFeignService;
import com.warpfuture.iot.api.console.service.MerchantConsoleService;
import com.warpfuture.iot.api.console.util.AliOssUtil;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class MerchantConsoleServiceImpl implements MerchantConsoleService {

    @Autowired
    private MerchantConsoleFeignService merchantConsoleFeignService;

    @Override
    public ResultVO createMerchant(Merchant merchant) {
        ResultVO result;
        try {
            merchant = sendWXToAliOss(merchant);
            merchant.setFile(null);
            result = merchantConsoleFeignService.createMerchant(merchant);
        } catch (SendAliOssException e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail("Error: " + e.getMessage());
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateMerchant(Merchant merchant) {
        ResultVO result;
        try {
            merchant = sendWXToAliOss(merchant);
            merchant.setFile(null);
            result = merchantConsoleFeignService.updateMerchant(merchant);
        } catch (SendAliOssException e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail("Error: " + e.getMessage());
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO deleteMerchant(Merchant merchant) {
        ResultVO result;
        try {
            result = merchantConsoleFeignService.deleteMerchant(merchant);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryMerchant(Merchant merchant) {
        ResultVO result;
        try {
            result = merchantConsoleFeignService.queryMerchant(merchant);
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryMerchantByAccountId(String accountId, PageModel pageModel) {
        ResultVO result;
        try {
            result = merchantConsoleFeignService.queryMerchantByAccountId(accountId, pageModel.getPageIndex(), pageModel.getPageSize());
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    /**
     * 将微信退款证书上传到AliOss
     *
     * @param merchant
     * @return
     */
    private Merchant sendWXToAliOss(Merchant merchant) throws SendAliOssException {
        if (merchant.getWxPayData() != null) {
            MultipartFile file = merchant.getFile();
            if (file != null) {
                WxpayData wxPayData = merchant.getWxPayData();
                AliResult result = AliOssUtil.putObject(file, merchant.getAccountId() + "-" + wxPayData.getWxPayAppId(), AliProperty.certFileKey);
                if (result.isSuccess()) {
                    wxPayData.setWxPayRefundBook(result.getKey());
                }
            }
        }
        return merchant;
    }
}
