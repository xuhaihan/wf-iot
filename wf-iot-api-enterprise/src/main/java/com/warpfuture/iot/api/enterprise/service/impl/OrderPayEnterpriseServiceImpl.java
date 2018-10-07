package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.OrderOperationDto;
import com.warpfuture.iot.api.enterprise.feign.service.OrderPayEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.OrderPayEnterpriseService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OrderPayEnterpriseServiceImpl implements OrderPayEnterpriseService {

    @Autowired
    private OrderPayEnterpriseFeignService orderPayEnterpriseFeignService;

    @Override
    public ResultVO<Map<String, String>> create(OrderOperationDto orderOperationDto) {
        ResultVO<Map<String, String>> result;
        try {
            result = orderPayEnterpriseFeignService.create(orderOperationDto);
        } catch (Exception e) {
            log.warn("Request Pay Error, create( {} ): {}", orderOperationDto, e.getMessage());
            return new ResultVO().fail(ResponseMsg.CREATE_ORDER_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

}
