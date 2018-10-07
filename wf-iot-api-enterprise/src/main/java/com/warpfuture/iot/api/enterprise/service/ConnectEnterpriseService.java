package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.dto.ConnectAutoDto;
import com.warpfuture.vo.ResultVO;

public interface ConnectEnterpriseService {

    ResultVO requestConnect(ConnectAutoDto connectAutoDto);

    ResultVO removeConnect(ConnectAutoDto connectAutoDto);

}
