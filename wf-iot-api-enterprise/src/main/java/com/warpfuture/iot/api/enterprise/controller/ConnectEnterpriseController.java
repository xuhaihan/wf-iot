package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.ConnectAutoDto;
import com.warpfuture.iot.api.enterprise.service.ConnectEnterpriseService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.ModelBeanUtil;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ConnectEnterpriseController {

    @Autowired
    private ConnectEnterpriseService connectEnterpriseService;

    @PostMapping(value = "/connect/request")
    public ResultVO requestConnect(Model model) {
        ConnectAutoDto connectAutoDto = ModelBeanUtil.getBeanFromModel(model, "connectAutoDto", ConnectAutoDto.class);

        if (CompareUtil.connectAutoDtoNotNull(connectAutoDto) && connectAutoDto.getKeepTime() != null && connectAutoDto.getStartTime() != null) {
            return connectEnterpriseService.requestConnect(connectAutoDto);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @PostMapping(value = "/connect/remove")
    public ResultVO removeConnect(Model model) {
        ConnectAutoDto connectAutoDto = ModelBeanUtil.getBeanFromModel(model, "connectAutoDto", ConnectAutoDto.class);

        if (CompareUtil.connectAutoDtoNotNull(connectAutoDto)) {
            return connectEnterpriseService.removeConnect(connectAutoDto);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

    @ModelAttribute(value = "connectAutoDto")
    public ConnectAutoDto setAccountId(@RequestBody ConnectAutoDto connectAutoDto, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (connectAutoDto != null && CompareUtil.strNotNull(accountId)) {
            connectAutoDto.setAccountId(accountId);
            String applicationId = String.valueOf(request.getAttribute("applicationId"));
            String userId = String.valueOf(request.getAttribute("userId"));
            if (StringUtils.isEmpty(connectAutoDto.getApplicationId())) {
                connectAutoDto.setApplicationId(applicationId);
            }
            if (StringUtils.isEmpty(connectAutoDto.getUserId())) {
                connectAutoDto.setUserId(userId);
            }
        }
        return connectAutoDto;
    }
}
