package com.warpfuture.iot.oauth.service.impl;

import com.warpfuture.entity.UserEntity;
import com.warpfuture.iot.oauth.entity.WeiXinUserInfo;
import com.warpfuture.iot.oauth.repository.UserRepository;
import com.warpfuture.iot.oauth.service.UserService;
import com.warpfuture.iot.oauth.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/** Created by 徐海瀚 on 2018/4/17. */
@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<UserEntity> implements UserService {

  private UserRepository userRepository;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.setBaseRepository(userRepository);
  }

  /**
   * 根据openid检查是否存在该用户，不存在新增用户
   *
   * @param accountId 企业id
   * @param applicationId 应用id
   * @param weiXinUserInfo 微信用户的基本信息实体
   * @return 返回参数用户jwt负载的信息
   */
  @Override
  public Map<String, Object> verifyWOAUser(
      String accountId, String applicationId,String openId, WeiXinUserInfo weiXinUserInfo) {
    Map<String, Object> map = new HashMap<>();
    UserEntity userEntity =null;
    if (!StringUtils.isEmpty(weiXinUserInfo.getUnionid())) {
        Criteria criteria = Criteria.where("wxData.unionId1").is(weiXinUserInfo.getUnionid());
        userEntity = userRepository.findOne(new Query(criteria));
        log.info("通过unionId1查询用户: {}",userEntity);
    } else if (!StringUtils.isEmpty(openId)){
        Criteria criteria = Criteria.where("wxData.h5.openId1").is(openId);
        userEntity = userRepository.findOne(new Query(criteria));
        log.info("通过openId查询用户: {}",userEntity);
    }
    log.info("===> verifyWOAUser( {}, {}, {} )  -->  查询用户: {}",accountId,applicationId,weiXinUserInfo,userEntity);
    if (userEntity != null) {
      map.put("accountId", userEntity.getAccountId());
      map.put("userId", userEntity.getUserId());
      map.put("applicationId", userEntity.getApplicationId());

      // 更新授权账号基本信息数据
      if (weiXinUserInfo.getNickname() != null) { // 未关注时,只有openid和unionid，其他为空
        userEntity.setNickname(weiXinUserInfo.getNickname());
        userEntity.getWxData().put("userInfo1", weiXinUserInfo);
        userRepository.save(userEntity);
      }
      if (userEntity.getWxData().get("unionId1") == null
          && weiXinUserInfo.getUnionid() != null) { // 判断当前的user是否有unionId
        userEntity
            .getWxData()
            .put("unionId1", weiXinUserInfo.getUnionid()); // 当有关联微信开放平台时，更新用户的unionId
        userRepository.save(userEntity);
      }
      log.info("---> 更新用户: {} ",userEntity);
    } else {
      map.put("accountId", accountId);
      map.put("applicationId", applicationId);
      String userId = UUIDUtil.get12UUID();
      map.put("userId", userId);
      userEntity = new UserEntity();
      userEntity.setAccountId(accountId);
      userEntity.setApplicationId(applicationId);
      userEntity.setUserId(userId);
      userEntity.setDelete(false);
      if (weiXinUserInfo.getNickname() != null)
        userEntity.setNickname(weiXinUserInfo.getNickname());
      Map<String, Object> map1 = new HashMap<>();
      Map<String, String> map2 = new HashMap<>(); // 存公众号对应openid
      map2.put("openId1", openId);
      map1.put("h5", map2);
      map1.put("unionId1", weiXinUserInfo.getUnionid()); // 不存在时为null
      map1.put("userInfo1", weiXinUserInfo); // 保存授权账户信息
      userEntity.setWxData(map1);
      log.info("---> 新增用户: {} ",userEntity);
      userRepository.save(userEntity);
    }
    return map;
  }

  @Override
  public Map<String, Object> findByOauthType(String oauthType, String oauthId) {
    String type = oauthType + "Data";
    Map<String, Object> map = new LinkedHashMap<>();
    UserEntity userEntity =
        userRepository.findOne(new Query(Criteria.where(type + ".unionId1").is(oauthId)));
    if (userEntity == null) { // 根据unionId找不到对应的用户
      userEntity =
          userRepository.findOne(new Query(Criteria.where(type + ".h5.openId1").is(oauthId)));
      if (userEntity == null) {
        userEntity =
            userRepository.findOne(new Query(Criteria.where(type + ".web.openId1").is(oauthId)));
        if (userEntity == null) {
          userEntity =
              userRepository.findOne(new Query(Criteria.where(type + ".app.openId1").is(oauthId)));
          if (userEntity != null) {
            map.put("type", type + ".app");
          }
        } else {
          map.put("type", type + ".web");
        }
      } else {
        map.put("type", type + ".h5");
      }
    } else {
      map.put("type", type + ".unionId"); // 表明已经绑定微信开放平台具有unionId
    }
    map.put("userEntity", userEntity);
    return map;
  }

  /**
   * 绑定具体类型 web、app、h5类型
   *
   * @param userEntity1 主动绑定者授权账号
   * @param oauthMap2 经过findByOauthType方法后返回的map类型数据,包含被动绑定账号数据
   * @return Boolean
   */
  @Override
  public Boolean bingBySpecificType(UserEntity userEntity1, Map<String, Object> oauthMap2) {
    try {
      String oauthType = (String) oauthMap2.get("type");
      UserEntity userEntity2 = (UserEntity) oauthMap2.get("userEntity");
      switch (oauthType) {
        case "wxData.h5":
          {
            Map<String, Object> activeH5 = (Map<String, Object>) userEntity1.getWxData().get("h5");
            Map<String, Object> wxData = userEntity1.getWxData();
            Map<String, Object> h5 = (Map<String, Object>) userEntity2.getWxData().get("h5");
            if (activeH5.containsKey("openId1")) {
              activeH5.put("openId2", h5.get("openId1"));
            } else {
              activeH5.put("openId1", h5.get("openId1"));
            }
            wxData.put("userInfo2", userEntity2.getWxData().get("userInfo1"));
            break;
          }
        case "wxData.web":
          {
            Map<String, Object> activeWeb =
                (Map<String, Object>) userEntity1.getWxData().get("web");
            Map<String, Object> wxData = userEntity1.getWxData();
            Map<String, Object> web = (Map<String, Object>) userEntity2.getWxData().get("web");
            if (activeWeb.containsKey("openId1")) {
              activeWeb.put("openId2", web.get("openId1"));
            } else {
              activeWeb.put("openId1", web.get("openId1"));
            }
            wxData.put("userInfo2", userEntity2.getWxData().get("userInfo1"));
            break;
          }
        case "wxData.app":
          {
            Map<String, Object> activeApp =
                (Map<String, Object>) userEntity1.getWxData().get("app");
            Map<String, Object> wxData = userEntity1.getWxData();
            Map<String, Object> app = (Map<String, Object>) userEntity2.getWxData().get("app");
            if (activeApp.containsKey("openId1")) {
              activeApp.put("openId2", app.get("openId1"));
            } else {
              activeApp.put("openId1", app.get("openId1"));
            }
            wxData.put("userInfo2", userEntity2.getWxData().get("userInfo1"));
            break;
          }
        case "qqData.app":
          {
            Map<String, Object> activeApp =
                (Map<String, Object>) userEntity1.getQqData().get("app");
            Map<String, Object> qqData = userEntity1.getQqData();
            Map<String, Object> app = (Map<String, Object>) userEntity2.getQqData().get("app");
            if (activeApp.containsKey("openId1")) {
              activeApp.put("openId2", app.get("openId1"));
            } else {
              activeApp.put("openId1", app.get("openId1"));
            }
            qqData.put("userInfo2", userEntity2.getQqData().get("userInfo1"));
            break;
          }
        case "qqData.web":
          {
            Map<String, Object> activeWeb =
                (Map<String, Object>) userEntity1.getQqData().get("web");
            Map<String, Object> qqData = userEntity1.getQqData();
            Map<String, Object> web = (Map<String, Object>) userEntity2.getQqData().get("web");
            if (activeWeb.containsKey("openId1")) {
              activeWeb.put("openId2", web.get("openId1"));
            } else {
              activeWeb.put("openId1", web.get("openId1"));
            }
            qqData.put("userInfo2", userEntity2.getQqData().get("userInfo1"));
            break;
          }
        case "wbData.web":
          {
            Map<String, Object> activeWeb =
                (Map<String, Object>) userEntity1.getWbData().get("web");
            Map<String, Object> wbData = userEntity1.getWbData();
            Map<String, Object> web = (Map<String, Object>) userEntity2.getWbData().get("web");
            if (activeWeb.containsKey("openId1")) {
              activeWeb.put("openId2", web.get("openId1"));
            } else {
              activeWeb.put("openId1", web.get("openId1"));
            }
            wbData.put("userInfo2", userEntity2.getWxData().get("userInfo1"));
            break;
          }
        case "wbData.app":
          {
            Map<String, Object> activeWeb =
                (Map<String, Object>) userEntity1.getWbData().get("web");
            Map<String, Object> wbData = userEntity1.getWbData();
            Map<String, Object> app = (Map<String, Object>) userEntity2.getWbData().get("app");
            activeWeb.put("openId2", app.get("openId1"));
            wbData.put("userInfo2", userEntity2.getWbData().get("userInfo1"));
            break;
          }
        case "wxData.unionId":
          {
            String unionId1 = (String) userEntity2.getWxData().get("unionId1");
            Map<String, Object> wxData = userEntity1.getWxData();
            wxData.put("unionId2", unionId1);
            wxData.put("userInfo2", userEntity2.getWbData().get("userInfo1"));
            break;
          }
        case "qqData.unionId":
          {
            String unionId1 = (String) userEntity2.getQqData().get("unionId1");
            Map<String, Object> qqData = userEntity1.getQqData();
            qqData.put("unionId2", unionId1);
            qqData.put("userInfo2", userEntity2.getQqData().get("userInfo1"));
            break;
          }
        case "wbData.unionId":
          {
            String unionId1 = (String) userEntity2.getWbData().get("unionId1");
            Map<String, Object> wbData = userEntity1.getWbData();
            wbData.put("unionId2", unionId1);
            wbData.put("userInfo2", userEntity2.getWbData().get("userInfo1"));
            break;
          }
        default:
          return false;
      }
    } catch (RuntimeException e) {
      log.error("绑定具体类型出错:{}", e.getMessage());
      return false;
    }
    return true;
  }

  /** 与另一个授权账号绑定动作 */
  @Override
  public Boolean bingOthers(Map<String, Object> map1, Map<String, Object> map2) {
    UserEntity userEntity1 = (UserEntity) map1.get("userEntity");
    UserEntity userEntity2 = (UserEntity) map2.get("userEntity");
    if (bingBySpecificType(userEntity1, map2)) {
      userEntity2.setDelete(true);
      userRepository.save(userEntity1);
      userRepository.save(userEntity2);
      return true;
    } else {
      return false;
    }
  }

  /**
   * @param oauthType2 解绑的授权账号类型
   * @param userEntity1 需要解绑的授权账号
   * @param userEntity2 解绑的授权账号
   */
  @Override
  public void unBingOthers(String oauthType2, UserEntity userEntity1, UserEntity userEntity2) {
    switch (oauthType2) {
      case "wxData.h5":
        {
          Map<String, Object> h5 = (Map<String, Object>) userEntity1.getWxData().get("h5");
          Map<String, Object> wxData = userEntity1.getWxData();
          if (h5.containsKey("openId1")) {
            h5.remove("openId2");
          } else {
            h5.remove("openId1");
          }
          wxData.remove("userInfo2");
          break;
        }
      case "wxData.web":
        {
          Map<String, Object> web = (Map<String, Object>) userEntity1.getWxData().get("web");
          Map<String, Object> wxData = userEntity1.getWxData();
          if (web.containsKey("openId1")) {
            web.remove("openId2");
          } else {
            web.remove("openId1");
          }
          wxData.remove("userInfo2");
          break;
        }
      case "wxData.app":
        {
          Map<String, Object> app = (Map<String, Object>) userEntity1.getWxData().get("app");
          Map<String, Object> wxData = userEntity1.getWxData();
          if (app.containsKey("openId1")) {
            app.remove("openId2");
          } else {
            app.remove("openId1");
          }
          wxData.remove("userInfo2");
          break;
        }
      case "qqData.web":
        {
          Map<String, Object> web = (Map<String, Object>) userEntity1.getQqData().get("web");
          Map<String, Object> qqData = userEntity1.getQqData();
          if (web.containsKey("openId1")) {
            web.remove("openId2");
          } else {
            web.remove("openId1");
          }
          qqData.remove("userInfo2");
          break;
        }
      case "qqData.app":
        {
          Map<String, Object> app = (Map<String, Object>) userEntity1.getQqData().get("app");
          Map<String, Object> qqData = userEntity1.getQqData();
          if (app.containsKey("openId1")) {
            app.remove("openId2");
          } else {
            app.remove("openId1");
          }
          qqData.remove("userInfo2");
          break;
        }
      case "wbData.web":
        {
          Map<String, Object> web = (Map<String, Object>) userEntity1.getWbData().get("web");
          Map<String, Object> wbData = userEntity1.getWbData();
          if (web.containsKey("openId1")) {
            web.remove("openId2");
          } else {
            web.remove("openId1");
          }
          wbData.remove("userInfo2");
          break;
        }
      case "wbData.app":
        {
          Map<String, Object> app = (Map<String, Object>) userEntity1.getWxData().get("app");
          Map<String, Object> wbData = userEntity1.getWbData();
          if (app.containsKey("openId1")) {
            app.remove("openId2");
          } else {
            app.remove("openId1");
          }
          wbData.remove("userInfo2");
          break;
        }
    }
    if (userEntity1.getExpand() != null) {
      String phone2 = (String) userEntity1.getExpand().get("phone2");
      if (phone2 != null) {
        userEntity1.getExpand().remove("phone2");
      }
    }
    userEntity2.setDelete(false);
    userRepository.save(userEntity1);
    userRepository.save(userEntity2);
  }
}
