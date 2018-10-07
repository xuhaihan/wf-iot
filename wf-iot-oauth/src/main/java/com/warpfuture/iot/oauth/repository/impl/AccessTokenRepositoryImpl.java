package com.warpfuture.iot.oauth.repository.impl;

import com.warpfuture.iot.oauth.entity.OrdinaryAccessToken;
import com.warpfuture.iot.oauth.repository.WxAccessTokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccessTokenRepositoryImpl extends BaseRepositoryImpl<OrdinaryAccessToken> implements WxAccessTokenRepository {
    @Override
    protected Class<OrdinaryAccessToken> getEntityClass() {
        return OrdinaryAccessToken.class;
    }
}
