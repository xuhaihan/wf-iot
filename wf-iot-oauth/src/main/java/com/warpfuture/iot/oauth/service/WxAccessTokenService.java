package com.warpfuture.iot.oauth.service;


import com.warpfuture.iot.oauth.entity.OrdinaryAccessToken;
import com.warpfuture.iot.oauth.service.base.BaseService;

import java.io.IOException;

public interface WxAccessTokenService extends BaseService<OrdinaryAccessToken> {
  OrdinaryAccessToken getOrdinaryAccessToken(String appId) throws IOException;

}
