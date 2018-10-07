package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.order.Order;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-production-service")
public interface OrderEnterpriseFeignService {

    @PostMapping(value = "/order/query")
    ResultVO<Order> query(@RequestBody Order order);

    @PostMapping(value = "/order/queryOrderList")
    ResultVO<PageModel<Order>> queryOrderList(@RequestBody Merchant merchant,
                                              @RequestParam(value = "pageSize") Integer pageSize,
                                              @RequestParam(value = "pageIndex") Integer pageIndex);

}
