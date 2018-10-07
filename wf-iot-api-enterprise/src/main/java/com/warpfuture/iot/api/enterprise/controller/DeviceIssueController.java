package com.warpfuture.iot.api.enterprise.controller;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.CloudCommunicateMsg;
import com.warpfuture.iot.api.enterprise.service.DeviceIssueService;
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
public class DeviceIssueController {

    @Autowired
    private DeviceIssueService deviceIssueService;

    /**
     * 向设备发送数据
     * @param cloudCommunicateMsg
     * @return
     */
    @PostMapping(value = "/issue")
    public ResultVO issue(@RequestBody CloudCommunicateMsg cloudCommunicateMsg, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));
        if (CompareUtil.strNotNull(accountId)){
            if (CompareUtil.communicateMsgNotNull(cloudCommunicateMsg)) {
                cloudCommunicateMsg.setAccountId(accountId);
                boolean res = deviceIssueService.issue(cloudCommunicateMsg);
                return res ? new ResultVO().success() : new ResultVO().fail(ResponseMsg.REQUEST_ERROR);
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

}
