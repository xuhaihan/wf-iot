package com.warpfuture.iot.api.enterprise.service.impl;

import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.order.Order;
import com.warpfuture.iot.api.enterprise.feign.service.OrderEnterpriseFeignService;
import com.warpfuture.iot.api.enterprise.service.OrderEnterpriseService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderEnterpriseServiceImpl implements OrderEnterpriseService {

    @Autowired
    private OrderEnterpriseFeignService orderEnterpriseFeignService;

    @Override
    public ResultVO query(Order order) {
        ResultVO<Order> result;
        try {
            result = orderEnterpriseFeignService.query(order);
        } catch (Exception e) {
            log.warn("Service Error : {}",e.getMessage());
            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
        }
        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
    }

//    @Override
//    public ResultVO queryOrderList(Merchant merchant, PageModel pageModel) {
//        ResultVO<PageModel<Order>> result;
//        try {
//            result = orderEnterpriseFeignService.queryOrderList(merchant, pageModel.getPageSize(), pageModel.getPageIndex());
//        } catch (Exception e) {
//            log.warn("Service Error : {}",e.getMessage());
//            return new ResultVO().fail(ResponseMsg.FEIGN_REQUEST_ERROR);
//        }
//        return result == null ? new ResultVO().fail(ResponseMsg.REQUEST_ERROR) : result;
//    }
}
