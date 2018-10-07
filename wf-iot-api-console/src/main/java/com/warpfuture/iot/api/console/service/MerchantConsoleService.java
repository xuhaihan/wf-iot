package com.warpfuture.iot.api.console.service;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.vo.ResultVO;

public interface MerchantConsoleService {

    ResultVO createMerchant(Merchant merchant);

    ResultVO updateMerchant(Merchant merchant);

    ResultVO deleteMerchant(Merchant merchant);

    ResultVO queryMerchant(Merchant merchant);

    ResultVO queryMerchantByAccountId(String accountId, PageModel pageModel);

}
