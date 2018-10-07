package com.warpfuture.iot.protocol.constant;

/** Created by 徐海瀚 on 2018/4/16. */
public class UrlStatic {

  public static final String VERIFY_USER_TOKEN = "http://wfoauth.free.ngrok.cc/oauth/verifyJwt";
  public static final String CHECK_DEVICE_WITHKEY = "http://localhost:8083/iot/auth/checkWithKey";
  public static final String CHECK_DEVICE_WITHSECURE =
      "http://localhost:8083/iot/auth/checkWithSecure";
  public static final String CHECK_USER_TO_DEVICE = "http://localhost:8083/iot/auth/userToDev";
  public static final String CHECK_DEVICE_TO_USER = "http://localhost:8083/iot/auth/devToUser";
}
