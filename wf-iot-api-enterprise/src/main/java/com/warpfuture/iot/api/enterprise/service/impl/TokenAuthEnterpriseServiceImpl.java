package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.RepayEntity;
import com.warpfuture.iot.api.enterprise.feign.service.TokenAuthEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.TokenAuthEnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TokenAuthEnterpriseServiceImpl implements TokenAuthEnterpriseService {

    @Autowired
    private TokenAuthEnterpriseFeignService tokenAuthEnterpriseFeignService;

    @Override
    public RepayEntity<Map<String, Object>> verifyJwt(String token) {
        RepayEntity<Map<String, Object>> result;
        try {
            result = tokenAuthEnterpriseFeignService.verifyJwt(token);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new RepayEntity<>().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new RepayEntity<>().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
