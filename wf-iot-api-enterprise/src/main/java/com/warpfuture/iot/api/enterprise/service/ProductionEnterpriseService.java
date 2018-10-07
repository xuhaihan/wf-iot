package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.vo.ResultVO;

public interface ProductionEnterpriseService {

    ResultVO queryProductionByAccountId(String accountId, Integer pageSize, Integer pageIndex);

}
