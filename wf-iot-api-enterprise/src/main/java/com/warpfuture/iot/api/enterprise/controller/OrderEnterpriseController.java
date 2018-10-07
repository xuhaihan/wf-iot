package com.warpfuture.iot.api.enterprise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.dto.PayOrder;
import com.warpfuture.entity.UserEntity;
import com.warpfuture.entity.order.Order;
import com.warpfuture.iot.api.enterprise.service.OrderEnterpriseService;
import com.warpfuture.iot.api.enterprise.service.OrderPayEnterpriseService;
import com.warpfuture.iot.api.enterprise.service.TokenAuthEnterpriseService;
import com.warpfuture.iot.api.enterprise.service.UserEnterpriseService;
import com.warpfuture.util.CompareUtil;
import com.warpfuture.util.JwtCommonUtil;
import com.warpfuture.vo.ResultVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
public class OrderEnterpriseController {

    @Autowired
    private OrderEnterpriseService orderEnterpriseService;

    @Autowired
    private OrderPayEnterpriseService orderPayEnterpriseService;

    @Autowired
    private TokenAuthEnterpriseService tokenAuthEnterpriseService;

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Autowired
    private ObjectMapper mapper;

    @GetMapping(value = "/order/{merchantId}/{tradeNumber}")
    public ResultVO query(@PathVariable String merchantId, @PathVariable String tradeNumber, HttpServletRequest request) {
        String accountId = String.valueOf(request.getAttribute("accountId"));

        if (CompareUtil.strNotNull(merchantId) && CompareUtil.strNotNull(tradeNumber) && CompareUtil.strNotNull(accountId)) {
            Order order = new Order();
            order.setMerchantId(merchantId);
            order.setMerchantTradeNumber(tradeNumber);
            order.setAccountId(accountId);
            return orderEnterpriseService.query(order);
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

//    @GetMapping(value = "/order/{mid}")
//    public ResultVO queryOrderList(@PathVariable String mid, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageIndex, HttpServletRequest request) {
//        String accountId = String.valueOf(request.getAttribute("accountId"));
//        Merchant merchant = new Merchant(accountId, mid);
//        if (CompareUtil.merchantIdNotNull(merchant)) {
//            PageModel pageModel = PageUtils.dealPage(pageSize, pageIndex, PageConstant.ORDER_DEFAULT_SIZE);
//            return orderEnterpriseService.queryOrderList(merchant, pageModel);
//        }
//        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
//    }

    @PostMapping("/order")
    public ResultVO createOrder(@RequestBody PayOrder payOrder) {
        if (CompareUtil.createOrderNotNull(payOrder)) {
            Claims claims = JwtCommonUtil.parserJsonWebToken(payOrder.getParams().getToken());
            log.info("======> User Claims : {}", claims);
            if (claims != null && claims.getExpiration().after(new Date())) {
                String accountId = String.valueOf(claims.get("accountId"));
                String userId = String.valueOf(claims.get("userId"));
                String applicationId = String.valueOf(claims.get("applicationId"));
                ResultVO<UserEntity> resultVO = userEnterpriseService.queryUser(new UserEntity(accountId, applicationId, userId));
                log.info("======> User Result : {}", resultVO);

                if (resultVO.isSuccess()) {
                    UserEntity userEntity = resultVO.getData();
                    payOrder.setAccountId(userEntity.getAccountId());
                    Object h5 = userEntity.getWxData().get("h5");
                    String openId = "";
                    if (h5 != null && h5 instanceof Map) {
                        Object obj = ((Map) h5).get("openId1");
                        if (obj != null) {
                            openId = String.valueOf(obj);
                        }
                    }

                    log.info("---> OpenId: {}", openId);
                    if (CompareUtil.strNotNull(openId)) {
                        payOrder.getParams().setOpenid(openId);
                        ResultVO<Map<String, String>> orderResult = orderPayEnterpriseService.create(payOrder.toOrder());
                        if (orderResult.isSuccess()) {
                            String userStr = "";
                            try {
                                userStr = mapper.writeValueAsString(userEntity);
                            } catch (JsonProcessingException e) {
                                log.error("用户实体json转换失败 -> {} : 结果 : {} -> {}", e, userEntity, userStr);
                            }
                            orderResult.getData().put("userEntity", userStr);
                        }
                        return orderResult;
                    }
                }
            }
        }
        return new ResultVO().fail(ResponseMsg.PARAMS_NULL);
    }

}
