package com.warpfuture.iot.api.console.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.iot.api.console.feign.service.OAuthConsoleFeignService;
import com.warpfuture.iot.api.console.service.OAuthConsoleService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OAuthConsoleServiceImpl implements OAuthConsoleService {

    @Autowired
    private OAuthConsoleFeignService oAuthConsoleFeignService;

    @Override
    public ResultVO getWOAUrl(ApplicationEntity applicationEntity) {
        ResultVO result;
        try {
            result = oAuthConsoleFeignService.getWOAUrl(applicationEntity.getApplicationId(), applicationEntity.getAccountId());
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

}
