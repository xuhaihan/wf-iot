package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.vo.ResultVO;

public interface MerchantEnterpriseService {

    ResultVO queryMerchant(Merchant merchant);

    ResultVO queryMerchantByAccountId(String accountId, PageModel pageModel);

}
