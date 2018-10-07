package com.warpfuture.iot.api.console.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.iot.api.console.service.ProductConsoleService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.ModelBeanUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProductConsoleController {

    @Autowired
    private ProductConsoleService productConsoleService;

    @PostMapping(value = "/production/create")
    public ResultVO createProduct(Model model) {
        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);

        if (CompareUtil.productionNotNull(production)) {
            return productConsoleService.createProduction(production);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/production/update")
    public ResultVO updateProduct(Model model) {
        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);

        if (CompareUtil.productionIdNotNull(production) && CompareUtil.productionBasicNotNull(production)) {
            return productConsoleService.updateProduction(production);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/production/delete")
    public ResultVO deleteProduct(Model model) {
        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);

        if (CompareUtil.productionIdNotNull(production)) {
            return productConsoleService.deleteProduction(production);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/production/query")
    public ResultVO queryProduction(Model model) {
        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);

        if (CompareUtil.productionIdNotNull(production)) {
            return productConsoleService.queryProduction(production);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/production/account/query")
    public ResultVO queryProductionByAccountId(HttpServletRequest request,
                                               @RequestParam(required = false) Integer pageSize,
                                               @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.PRODUCTION_DEFAULT_PAGE_SIZE);
            return productConsoleService.queryProductionByAccountId(accountId, pageModel.getPageSize(), pageModel.getPageIndex());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/production/down")
    public ResultVO dropOffProduction(Model model) {
        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);

        if (CompareUtil.productionIdNotNull(production)) {
            return productConsoleService.revoke(production);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/production/publish")
    public ResultVO publishProduction(Model model) {
        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);

        if (CompareUtil.productionIdNotNull(production)) {
            return productConsoleService.publish(production);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/production/updateExtensions")
    public ResultVO updateExtensions(Model model) {
        Production production = ModelBeanUtil.getBeanFromModel(model, "production", Production.class);

        if (CompareUtil.productionIdNotNull(production)) {
            return productConsoleService.updateExtensions(production);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @ModelAttribute(value = "production")
    public Production setAccountId(@RequestBody(required = false) Production production, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (production != null && !StringUtils.isEmpty(accountId)) {
            production.setAccountId(accountId);
        }
        return production;
    }

}
