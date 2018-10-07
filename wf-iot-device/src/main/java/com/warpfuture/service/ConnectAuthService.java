package com.warpfuture.service;

/** Created by fido on 2018/4/14. 建立用户-设备权限 */
public interface ConnectAuthService {
  public boolean addConnectAuth(
      String accountId,
      String porductionId,
      String deviceId,
      String userId,
      String applicationId,
      Long keepTime,
      Long startTime);

  public boolean removeConnectAuth(
      String accountId, String porductionId, String deviceId, String userId, String applicationId);
}
