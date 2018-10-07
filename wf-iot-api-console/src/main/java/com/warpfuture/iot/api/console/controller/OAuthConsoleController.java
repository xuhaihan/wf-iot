package com.warpfuture.iot.api.console.controller;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.iot.api.console.service.OAuthConsoleService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.ModelBeanUtil;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OAuthConsoleController {

    @Autowired
    private OAuthConsoleService oAuthConsoleService;

    @PostMapping(value = "/oauth/woaUrl")
    public ResultVO getWOAUrl(Model model) {
        ApplicationEntity applicationEntity = ModelBeanUtil.getBeanFromModel(model, "applicationEntity", ApplicationEntity.class);

        if (CompareUtil.applicationIdNotNull(applicationEntity)) {
            return oAuthConsoleService.getWOAUrl(applicationEntity);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @ModelAttribute(value = "applicationEntity")
    public ApplicationEntity setAccountId(@RequestBody ApplicationEntity applicationEntity, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));

        if (applicationEntity != null&&!StringUtils.isEmpty(accountId)) {
            applicationEntity.setAccountId(accountId);
        }
        return applicationEntity;
    }

}
