package com.warpfuture.iot.oauth.controller;

import com.warpfuture.entity.UserEntity;
import com.warpfuture.iot.oauth.exception.QueryFailException;
import com.warpfuture.iot.oauth.service.UserService;
import com.warpfuture.vo.ResultVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bing")
@Slf4j
public class BingController {

  @Autowired private UserService userService;

  /**
   * 授权账户与手机账号绑定
   *
   * @param oauthType 当前账户授权类型
   * @param oauthId 当前授权账户id(unionId或者openId,某种授权类型用户id)
   * @param phone 手机号
   * @return
   */
  @RequestMapping(value = "/phone", method = RequestMethod.POST)
  public ResultVO bingByPhone(
      @NonNull @RequestParam("oauthType") String oauthType,
      @NonNull @RequestParam("oauthId") String oauthId,
      @NonNull @RequestParam("phone") String phone) {
    ResultVO resultVO = new ResultVO();
    UserEntity userEntity = null;
    try {
      Map<String, Object> map = userService.findByOauthType(oauthType, oauthId);
      if (map != null && !map.isEmpty()) {
        userEntity = (UserEntity) map.get("userEntity");
      }
      if (userEntity != null && !userEntity.getDelete()) {
        if (userEntity.getPhone() != null) {
          if (phone.equals(userEntity.getPhone())) {
            resultVO.setCode(0);
            resultVO.setMessage("该账号已经绑定该手机号码");
          } else {
            userEntity.setPhone(phone);
            userService.save(userEntity);
            resultVO.setCode(0);
            resultVO.setMessage("更换手机号成功");
            return resultVO;
          }
        } else {
          UserEntity userEntity1 =
              userService.findOne(new Query(Criteria.where("phone").is(phone)));
          if (userEntity1 != null) { // 换绑
            if (!userEntity1.getDelete()) {
              userEntity1.setDelete(true);
              userService.save(userEntity1);
            }
            userEntity.setPhone(phone);
            userService.save(userEntity);
            resultVO.setCode(1);
            resultVO.setMessage("绑定成功");
            return resultVO;
          } else { // 直接绑定
            userEntity.setPhone(phone);
            userService.save(userEntity);
            resultVO.setCode(1);
            resultVO.setMessage("绑定成功");
            return resultVO;
          }
        }
      } else {
        resultVO.setCode(0);
        resultVO.setMessage("授权账号不存在");
      }
    } catch (QueryFailException e) {
      log.error("查询异常:{}", e.getMessage());
    }
    return resultVO;
  }

  /**
   * 授权账号绑定
   *
   * @param oauthType1 主动绑定的授权账号类型 wx,wb,qq
   * @param oauthId1 主动绑定的的授权账号id
   * @param oauthType2 被动绑定的授权账号类型
   * @param oauthId2 被动绑定的授权账号id
   * @return
   */
  @RequestMapping(value = "/oauth", method = RequestMethod.POST)
  public ResultVO bingByOauth(
      @NonNull @RequestParam("oauthType1") String oauthType1,
      @NonNull @RequestParam("oauthId1") String oauthId1,
      @NonNull @RequestParam("oauthType2") String oauthType2,
      @NonNull @RequestParam("oauthId2") String oauthId2) {
    ResultVO resultVO = new ResultVO();
    UserEntity userEntity1 = null;
    UserEntity userEntity2 = null;
    Map<String, Object> map1 = userService.findByOauthType(oauthType1, oauthId1);
    Map<String, Object> map2 = userService.findByOauthType(oauthType2, oauthId2);
    if (map1 != null && !map1.isEmpty()) {
      userEntity1 = (UserEntity) map1.get("userEntity");
    }
    if (map2 != null && !map2.isEmpty()) {
      userEntity2 = (UserEntity) map2.get("userEntity");
    }
    if (userEntity1 != null
        && !userEntity1.getDelete()
        && userEntity2 != null
        && !userEntity2.getDelete()) {
      String phone1 = userEntity1.getPhone();
      String phone2 = userEntity2.getPhone();
      if (phone1 != null && phone2 != null) { // 两个授权账户同时存在手机号码
        if (userEntity1.getExpand() != null) {
          userEntity1.getExpand().put("phone2", phone2);
        } else {
          Map<String, Object> extension = new HashMap<>();
          extension.put("phone2", phone2);
          userEntity1.setExpand(extension);
        }
        if (userService.bingOthers(map1, map2)) {
          resultVO.setCode(1);
          resultVO.setMessage("绑定成功");
          return resultVO;
        } else {
          resultVO.fail("授权账号绑定失败");
        }

      } else if (phone1 == null && phone2 == null) { // 两个授权账号都没有账号
        resultVO.setCode(0);
        resultVO.setMessage("两个授权账号没有绑定手机");

      } else { // 只有其中一个授权账户存在手机号
        if (phone1 != null) {
          if (userService.bingOthers(map1, map2)) { // 以有手机号1的实体1为主动绑定者
            resultVO.setCode(1);
            resultVO.setMessage("绑定成功");
            return resultVO;
          } else {
            resultVO.fail("绑定失败");
          }
        } else {
          if (userService.bingOthers(map2, map1)) {; // 以有手机号2的实体2为主动绑定者
            resultVO.setCode(1);
            resultVO.setMessage("绑定成功");
            return resultVO;
          } else {
            resultVO.fail("绑定失败");
          }
        }
      }
    } else {
      resultVO.setCode(0);
      resultVO.setMessage("主动绑定账号或者被动绑定的账号不能为空");
    }
    return resultVO;
  }
}
