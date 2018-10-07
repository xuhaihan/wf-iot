package com.warpfuture.iot.api.enterprise.feign.service;

import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wf-iot-production-service")
public interface MerchantEnterpriseFeignService {

    @PostMapping(value = "/merchant/queryByAccount")
    ResultVO queryMerchantByAccountId(@RequestParam(value = "accountId") String accountId,
                                      @RequestParam(value = "pageIndex") Integer pageIndex,
                                      @RequestParam(value = "pageSize") Integer pageSize);

    @PostMapping(value = "/merchant/query")
    ResultVO queryMerchant(@RequestBody Merchant merchant);
}
