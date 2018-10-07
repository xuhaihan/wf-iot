package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.iot.api.enterprise.service.MerchantEnterpriseService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MerchantEnterpriseController {

    @Autowired
    private MerchantEnterpriseService merchantEnterpriseService;

    @GetMapping(value = "/merchant")
    public ResultVO queryMerchantByAccountId(HttpServletRequest request, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.MERCHANT_DEFAULT_PAGE_SIZE);
            return merchantEnterpriseService.queryMerchantByAccountId(accountId, pageModel);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @GetMapping(value = "/merchant/{merchantId}")
    public ResultVO queryMerchant(@PathVariable String merchantId, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        Merchant merchant = new Merchant(accountId, merchantId);
        if (CompareUtil.merchantIdNotNull(merchant)) {
            return merchantEnterpriseService.queryMerchant(merchant);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

}
