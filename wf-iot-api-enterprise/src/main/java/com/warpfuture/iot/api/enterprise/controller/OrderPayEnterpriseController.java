//package com.warpfuture.iot.api.enterprise.controller;
//
//import com.warpfuture.constant.ResponseMsg;
//import com.warpfuture.dto.OrderOperationDto;
//import com.warpfuture.entity.UserEntity;
//import com.warpfuture.iot.api.enterprise.service.OrderPayEnterpriseService;
//import com.warpfuture.iot.api.enterprise.service.UserEnterpriseService;
//import com.warpfuture.util.CompareUtil;
//import com.warpfuture.vo.ResultVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
//@RestController
//public class OrderPayEnterpriseController {
//
//    @Autowired
//    private OrderPayEnterpriseService orderPayEnterpriseService;
//
//    @PostMapping(value = "/createOrder")
//    public ResultVO create(HttpServletRequest request, @RequestBody OrderOperationDto orderOperationDto) {
//        String accountId = String.valueOf(request.getAttribute("accountId"));
//        if (CompareUtil.strNotNull(accountId) &&
//                orderOperationDto != null &&
//                CompareUtil.strNotNull(orderOperationDto.getMerchantId()) &&
//                orderOperationDto.getParams() != null &&
//                !orderOperationDto.getParams().isEmpty()) {
//            orderOperationDto.setAccountId(accountId);
//            ResultVO<Map<String, String>> orderResult = orderPayEnterpriseService.create(orderOperationDto);
//            return orderResult;
//        }
//        return new ResultVO<>().fail(ResponseMsg.REQUEST_ERROR);
//    }
//
//}
