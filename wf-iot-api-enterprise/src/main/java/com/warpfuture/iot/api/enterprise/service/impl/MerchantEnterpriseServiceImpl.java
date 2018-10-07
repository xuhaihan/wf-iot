package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.iot.api.enterprise.feign.service.MerchantEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.MerchantEnterpriseService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MerchantEnterpriseServiceImpl implements MerchantEnterpriseService {

    @Autowired
    private MerchantEnterpriseFeignService merchantEnterpriseFeignService;

    @Override
    public ResultVO queryMerchant(Merchant merchant) {
        ResultVO result;
        try {
            result = merchantEnterpriseFeignService.queryMerchant(merchant);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryMerchantByAccountId(String accountId, PageModel pageModel) {
        ResultVO result;
        try {
            result = merchantEnterpriseFeignService.queryMerchantByAccountId(accountId, pageModel.getPageIndex(), pageModel.getPageSize());
        } catch (Exception e) {
            log.warn("Service Error : {}", e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
