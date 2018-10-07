package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.ConnectAutoDto;
import com.warpfuture.iot.api.enterprise.feign.service.ConnectEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.ConnectEnterpriseService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConnectEnterpriseServiceImpl implements ConnectEnterpriseService {

    @Autowired
    private ConnectEnterpriseFeignService connectEnterpriseFeignService;

    @Override
    public ResultVO requestConnect(ConnectAutoDto connectAutoDto) {
        ResultVO result;
        try {
            result = connectEnterpriseFeignService.requestConnect(connectAutoDto);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

    @Override
    public ResultVO removeConnect(ConnectAutoDto connectAutoDto) {
        ResultVO result;
        try {
            result = connectEnterpriseFeignService.removeConnect(connectAutoDto);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }
}
