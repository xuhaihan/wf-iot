package com.warpfuture.iot.oauth.constant;

/** Created by 徐海瀚 on 2018/4/21. */
public class OauthUrl {
  // 获得微信授权的access_token
  public static final String GET_WX_ACCESS_TOKEN =
      "https://api.weixin.qq.com/sns/oauth2/access_token?";
  public static final String GET_WX_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?";
  public static final String GET_WX_OAUTH_URL =
      "https://open.weixin.qq.com/connect/oauth2/authorize";
  // 配置js sdk的接口权限验证需要的access_token
  public static final String GET_WX_JS_ACCESS_TOKEN =
      "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
  public static final String GET_WX_JS_TICKET="https://api.weixin.qq.com/cgi-bin/ticket/getticket?";
 /* public static final String GET_WX_JS_TICKET =
      "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?";*/
 public static final String STATE_SPLIT_CHAR = "~~~";
}
