package com.warpfuture.iot.api.console.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.Production;
import com.warpfuture.iot.api.console.feign.service.ProductConsoleFeignService;
import com.warpfuture.iot.api.console.service.ProductConsoleService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductConsoleServiceImpl implements ProductConsoleService {

    @Autowired
    private ProductConsoleFeignService productConsoleFeignService;

    @Override
    public ResultVO createProduction(Production production) {
        ResultVO result;
        try {
            result = productConsoleFeignService.createProduction(production);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateProduction(Production production) {
        ResultVO result;
        try {
            result = productConsoleFeignService.updateProduction(production);
        } catch (Exception e) {
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO deleteProduction(Production production) {
        ResultVO result;
        try {
            result = productConsoleFeignService.deleteProduction(production);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryProduction(Production production) {
        ResultVO result;
        try {
            result = productConsoleFeignService.queryProduction(production);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO queryProductionByAccountId(String accountId, Integer pageSize, Integer pageIndex) {
        ResultVO result;
        try {
            result = productConsoleFeignService.queryProductionByAccountId(accountId, pageSize, pageIndex);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO revoke(Production production) {
        ResultVO result;
        try {
            result = productConsoleFeignService.revoke(production);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO publish(Production production) {
        ResultVO result;
        try {
            result = productConsoleFeignService.publish(production);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO updateExtensions(Production production) {
        ResultVO result;
        try {
            result = productConsoleFeignService.updateExtensions(production);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
