package com.warpfuture.iot.oauth.service;

import com.warpfuture.entity.UserEntity;
import com.warpfuture.iot.oauth.entity.WeiXinUserInfo;
import com.warpfuture.iot.oauth.service.base.BaseService;

import java.util.Map;

/** Created by 徐海瀚 on 2018/4/17. */
public interface UserService extends BaseService<UserEntity> {
  Map<String, Object> verifyWOAUser(
      String accountId, String applicationId,String openId, WeiXinUserInfo weiXinUserInfo);

  Map<String, Object> findByOauthType(String oauthType, String oauthId);

  Boolean bingBySpecificType(UserEntity userEntity1, Map<String, Object> oauthMap2);

  Boolean bingOthers(Map<String, Object> map1, Map<String, Object> map2);
  void unBingOthers(String oauthType2,UserEntity userEntity1,UserEntity userEntity2);
}
