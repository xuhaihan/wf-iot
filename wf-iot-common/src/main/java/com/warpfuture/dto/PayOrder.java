package com.warpfuture.dto;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString
@Slf4j
public class PayOrder {

    private String accountId;

    private String merchantId;

    private Params params = new Params();

    public OrderOperationDto toOrder() {
        OrderOperationDto orderOperationDto = new OrderOperationDto();
        orderOperationDto.setMerchantId(this.merchantId);
        orderOperationDto.setAccountId(this.accountId);
        log.info("==========> PayOrder: {}", this);
        Map<String, String> map = new HashMap<>();
        map.put("body", this.params.getBody());
        map.put("out_trade_no", this.params.getOut_trade_no());
        map.put("total_fee", this.params.getTotal_fee());
        map.put("trade_type", this.params.getTrade_type());
        map.put("openid", this.params.getOpenid());
        orderOperationDto.setParams(map);
        return orderOperationDto;
    }

    @Data
    @ToString
    public class Params {

        private String body;

        private String out_trade_no;

        private String total_fee;//总金额，以 分 为单位

        private String trade_type;

        private String openid;

        private String token;
    }
}
