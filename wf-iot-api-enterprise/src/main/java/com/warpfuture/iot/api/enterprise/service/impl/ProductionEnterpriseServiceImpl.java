package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.iot.api.enterprise.feign.service.ProductEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.ProductionEnterpriseService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductionEnterpriseServiceImpl implements ProductionEnterpriseService {

    @Autowired
    private ProductEnterpriseFeignService productEnterpriseFeignService;

    @Override
    public ResultVO queryProductionByAccountId(String accountId, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = productEnterpriseFeignService.queryProductionByAccountId(accountId, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
