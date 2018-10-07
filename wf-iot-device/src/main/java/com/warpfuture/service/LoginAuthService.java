package com.warpfuture.service;

import java.util.Map;

/** Created by fido on 2018/4/13. */
public interface LoginAuthService {

  public Map<String, Object> checkWithKey(String productionKey, String deviceId);

  public Map<String, Object> checkWithSecure(String pksToken, String mode);
}
