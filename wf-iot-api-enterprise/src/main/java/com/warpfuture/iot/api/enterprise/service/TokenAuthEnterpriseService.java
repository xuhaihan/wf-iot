package com.warpfuture.iot.api.enterprise.service;

import com.warpfuture.entity.RepayEntity;

import java.util.Map;

public interface TokenAuthEnterpriseService {

    RepayEntity<Map<String, Object>> verifyJwt(String token);

}
