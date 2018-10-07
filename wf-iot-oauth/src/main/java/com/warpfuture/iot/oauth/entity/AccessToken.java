package com.warpfuture.iot.oauth.entity;

import lombok.ToString;

/** 微信授权的保存access_token等信息的实体 */
@ToString
public class AccessToken {
  private String access_token;//微信授权获得的令牌
  private int expires_in;//有效时间
  private String refresh_token;//刷新后时间久的access_token
  private String openid;//微信用户id
  private String scope;

  public AccessToken() {}

  public AccessToken(
      String access_token, int expires_in, String refresh_token, String openid, String scope) {
    this.access_token = access_token;
    this.expires_in = expires_in;
    this.refresh_token = refresh_token;
    this.openid = openid;
    this.scope = scope;
  }

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public int getExpires_in() {
    return expires_in;
  }

  public void setExpires_in(int expires_in) {
    this.expires_in = expires_in;
  }

  public String getRefresh_token() {
    return refresh_token;
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public String getOpenId() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
