package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.dto.OrderOperationDto;
import com.warpfuture.entity.order.Order;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient("wf-iot-pay-service")
public interface OrderPayEnterpriseFeignService {

    @RequestMapping(value = "/wxpay/createOrder", method = RequestMethod.POST)
    ResultVO<Map<String, String>> create(@RequestBody OrderOperationDto orderOperationDto);

    @RequestMapping(value = "/wxpay/createRefund", method = RequestMethod.POST)
    ResultVO<Order> createRefund(@RequestBody OrderOperationDto orderOperationDto);

}
