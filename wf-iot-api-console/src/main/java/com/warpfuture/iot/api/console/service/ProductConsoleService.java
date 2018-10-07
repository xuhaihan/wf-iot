package com.warpfuture.iot.api.console.service;

import com.warpfuture.entity.Production;
import com.warpfuture.vo.ResultVO;

public interface ProductConsoleService {

    ResultVO createProduction(Production production);

    ResultVO updateProduction(Production production);

    ResultVO deleteProduction(Production production);

    ResultVO queryProduction(Production production);

    ResultVO queryProductionByAccountId(String accountId, Integer pageSize, Integer pageIndex);

    ResultVO revoke(Production production);

    ResultVO publish(Production production);

    ResultVO updateExtensions(Production production);

}
