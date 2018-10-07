package com.warpfuture.iot.api.console.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.iot.api.console.dto.MerchantFront;
import com.warpfuture.iot.api.console.service.MerchantConsoleService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MerchantConsoleController {

    @Autowired
    private MerchantConsoleService merchantConsoleService;

    @PostMapping(value = "/merchant/create")
    public ResultVO createMerchant(HttpServletRequest request, MerchantFront merchantFront) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && merchantFront != null) {
            Merchant merchant = merchantFront.toMerchant();
            merchant.setAccountId(accountId);
            if (CompareUtil.merchantCreateNotNull(merchant)) {
                return merchantConsoleService.createMerchant(merchant);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/merchant/update")
    public ResultVO updateMerchant(MerchantFront merchantFront, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && merchantFront != null) {
            Merchant merchant = merchantFront.toMerchant();
            merchant.setAccountId(accountId);
            if (CompareUtil.merchantCreateNotNull(merchant)) {
                return merchantConsoleService.updateMerchant(merchant);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/merchant/delete")
    public ResultVO deleteMerchant(@RequestBody Merchant merchant, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && merchant != null) {
            merchant.setAccountId(accountId);
            if (CompareUtil.merchantIdNotNull(merchant)) {
                return merchantConsoleService.deleteMerchant(merchant);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/merchant/query")
    public ResultVO queryMerchant(@RequestBody Merchant merchant, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && merchant != null) {
            merchant.setAccountId(accountId);
            if (CompareUtil.merchantIdNotNull(merchant)) {
                return merchantConsoleService.queryMerchant(merchant);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/merchant/queryByAccount")
    public ResultVO queryMerchantByAccountId(HttpServletRequest request, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.MERCHANT_DEFAULT_PAGE_SIZE);
            return merchantConsoleService.queryMerchantByAccountId(accountId, pageModel);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

}
