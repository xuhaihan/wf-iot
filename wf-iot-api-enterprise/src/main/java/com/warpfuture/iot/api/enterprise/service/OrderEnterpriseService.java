package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.entity.order.Order;
import com.warpfuture.vo.ResultVO;

public interface OrderEnterpriseService {

    ResultVO query(Order order);

//    ResultVO queryOrderList(Merchant merchant, PageModel pageModel);

}
