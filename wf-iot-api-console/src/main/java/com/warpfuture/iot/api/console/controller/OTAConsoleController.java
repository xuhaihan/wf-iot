package com.warpfuture.iot.api.console.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.OTAInfo;
import com.warpfuture.entity.PageModel;
import com.warpfuture.iot.api.console.service.OTAConsoleService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OTAConsoleController {

    @Autowired
    private OTAConsoleService otaConsoleService;

    @PostMapping(value = "/ota/create")
    public ResultVO createOTA(OTAInfo otaInfo, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && otaInfo != null) {
            otaInfo.setAccountId(accountId);
            if (CompareUtil.otaNotNull(otaInfo)) {
                return otaConsoleService.createOTA(otaInfo);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/ota/query")
    public ResultVO queryOTA(HttpServletRequest request, @RequestBody OTAInfo otaInfo,
                             @RequestParam(required = false) Integer pageSize,
                             @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && otaInfo != null) {
            otaInfo.setAccountId(accountId);
            if (CompareUtil.otaIdNotNull(otaInfo)) {
                PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.OTA_DEFAULT_PAGE_SIZE);
                return otaConsoleService.queryOTA(otaInfo,pageModel);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/ota/disable")
    public ResultVO disableOTA(HttpServletRequest request, @RequestBody OTAInfo otaInfo) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId) && otaInfo != null) {
            otaInfo.setAccountId(accountId);
            if (CompareUtil.otaIdNotNull(otaInfo) &&
                    !StringUtils.isEmpty(otaInfo.getOtaId())) {
                return otaConsoleService.disableOTA(otaInfo);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }


}
