package com.warpfuture.iot.api.console.controller;

import com.warpfuture.constant.PageConstant;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.entity.PageModel;
import com.warpfuture.iot.api.console.service.ApplicationConsoleService;
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
public class ApplicationConsoleController {

    @Autowired
    private ApplicationConsoleService applicationConsoleService;

    @PostMapping(value = "/application/add")
    public ResultVO createApplication(Model model) {
        ApplicationEntity applicationEntity = ModelBeanUtil.getBeanFromModel(model, "applicationEntity", ApplicationEntity.class);

        if (applicationEntity != null && !StringUtils.isEmpty(applicationEntity.getAccountId()) && CompareUtil.applicationBasicNotNull(applicationEntity)) {
            if (CompareUtil.applicationOAuthNotNull(applicationEntity)) {
                return applicationConsoleService.createApplication(applicationEntity);
            } else {
                return new ResultVO<>().fail(ResponseMsg.APPLICATION_OAUTH_NULL);
            }
        }
        return new ResultVO<ApplicationEntity>().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/application/update")
    public ResultVO updateApplication(Model model) {
        ApplicationEntity applicationEntity = ModelBeanUtil.getBeanFromModel(model, "applicationEntity", ApplicationEntity.class);

        if (CompareUtil.applicationIdNotNull(applicationEntity)) {
            return applicationConsoleService.updateApplication(applicationEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/application/query")
    public ResultVO queryApplication(Model model) {
        ApplicationEntity applicationEntity = ModelBeanUtil.getBeanFromModel(model, "applicationEntity", ApplicationEntity.class);

        if (CompareUtil.applicationIdNotNull(applicationEntity)) {
            return applicationConsoleService.queryApplication(applicationEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/application/delete")
    public ResultVO deleteApplication(Model model) {
        ApplicationEntity applicationEntity = ModelBeanUtil.getBeanFromModel(model, "applicationEntity", ApplicationEntity.class);

        if (applicationEntity != null && CompareUtil.applicationIdNotNull(applicationEntity)) {
            return applicationConsoleService.deleteApplication(applicationEntity.getApplicationId());
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/application/list")
    public ResultVO listApplication(HttpServletRequest request, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageIndex) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (!StringUtils.isEmpty(accountId)) {
            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.APPLICATION_DEFAULT_PAGE_SIZE);
            return applicationConsoleService.listApplication(accountId, pageModel);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/application/update/basic")
    public ResultVO updateAppBasic(Model model) {
        ApplicationEntity applicationEntity = ModelBeanUtil.getBeanFromModel(model, "applicationEntity", ApplicationEntity.class);

        if (CompareUtil.applicationIdNotNull(applicationEntity) && CompareUtil.applicationBasicNotNull(applicationEntity)) {
            return applicationConsoleService.updateAppBasic(applicationEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/application/update/expansion")
    public ResultVO updateAppExpansion(Model model) {
        ApplicationEntity applicationEntity = ModelBeanUtil.getBeanFromModel(model, "applicationEntity", ApplicationEntity.class);

        if (CompareUtil.applicationIdNotNull(applicationEntity) && CompareUtil.applicationExpansionNotNull(applicationEntity)) {
            return applicationConsoleService.updateAppExpansion(applicationEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/application/update/oauthways")
    public ResultVO updateAppOAuthWays(Model model) {
        ApplicationEntity applicationEntity = ModelBeanUtil.getBeanFromModel(model, "applicationEntity", ApplicationEntity.class);

        if (CompareUtil.applicationIdNotNull(applicationEntity) && CompareUtil.applicationOAuthNotNull(applicationEntity)) {
            return applicationConsoleService.updateAppOAuthWays(applicationEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @ModelAttribute(value = "applicationEntity")
    public ApplicationEntity setAccountId(@RequestBody(required = false) ApplicationEntity applicationEntity, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (applicationEntity != null && !StringUtils.isEmpty(accountId)) {
            applicationEntity.setAccountId(accountId);
        }
        return applicationEntity;
    }

}
