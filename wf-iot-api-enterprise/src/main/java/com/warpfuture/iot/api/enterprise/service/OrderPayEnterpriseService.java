package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.dto.OrderOperationDto;
import com.warpfuture.vo.ResultVO;

import java.util.Map;

public interface OrderPayEnterpriseService {

    ResultVO<Map<String, String>> create(OrderOperationDto orderOperationDto);

}
