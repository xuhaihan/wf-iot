package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.iot.api.enterprise.service.ProductionEnterpriseService;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProductionEnterpriseController {

    @Autowired
    private ProductionEnterpriseService productionEnterpriseService;

    @GetMapping(value = "/product")
    public ResultVO queryProductionByAccountId(HttpServletRequest request, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.PRODUCTION_DEFAULT_PAGE_SIZE);
            return productionEnterpriseService.queryProductionByAccountId(accountId, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }
}
