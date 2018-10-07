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
import java.util.Map;

@RestController
@RequestMapping("/unbing")
@Slf4j
public class UnBingController {

  @Autowired private UserService userService;

  /**
   * 授权类型与手机账号解绑
   *
   * @param oauthType wx ,wb,qq 指定该常量表示授权类型
   * @param oauthId 该授权类型能唯一确定用户的id unionId,openId
   * @return
   */
  @RequestMapping(value = "/phone", method = RequestMethod.POST)
  public ResultVO unBingByPhone(
      @NonNull @RequestParam("oauthType") String oauthType,
      @NonNull @RequestParam("oauthId") String oauthId) {
    ResultVO resultVO = new ResultVO();
    try {
      UserEntity userEntity = null;
      Map<String, Object> map = userService.findByOauthType(oauthType, oauthId);
      if (map != null && !map.isEmpty()) {
        userEntity = (UserEntity) map.get("userEntity");
      }
      if (userEntity != null && !userEntity.getDelete()) {
        if (userEntity.getPhone() != null) {
          UserEntity userEntity1 =
              userService.findOne(new Query(Criteria.where("phone").is(userEntity.getPhone())));
          if (userEntity1 != null && userEntity1.getDelete()) {
            userEntity1.setDelete(false);
            userService.save(userEntity1);
          }
          userEntity.setPhone(null);
          userService.save(userEntity);
          resultVO.setCode(1);
          resultVO.setMessage("解绑成功");
          return resultVO;
        } else {
          resultVO.setCode(0);
          resultVO.setMessage("该授权账号未绑定手机号");
        }
      } else {
        resultVO.setCode(0);
        resultVO.setMessage("该授权账号不存在");
      }
    } catch (QueryFailException e) {
      log.error("查询异常:{}", e.getMessage());
    }
    return resultVO;
  }

  /**
   * 授权账号解绑
   *
   * @param oauthType1 需要解绑的授权账号类型 wx,wb,qq
   * @param oauthId1 需要解绑的授权账号的id unionId或者openId
   * @param oauthType2 解绑的授权账号类型 wx，wb，qq
   * @param oauthId2 解绑的授权账号id unionId或者openId
   * @return
   */
  @RequestMapping(value = "/oauth", method = RequestMethod.POST)
  public ResultVO unBingByOauth(
      @NonNull @RequestParam("oauthType1") String oauthType1,
      @NonNull @RequestParam("oauthId1") String oauthId1,
      @NonNull @RequestParam("oauthType2") String oauthType2,
      @NonNull @RequestParam("oauthId2") String oauthId2) {
    ResultVO resultVO = new ResultVO();
    UserEntity userEntity1 = null;
    UserEntity userEntity2 = null;
    String type = null;
    Map<String, Object> map1 = userService.findByOauthType(oauthType1, oauthId1); // 需要解绑的账号
    Map<String, Object> map2 = userService.findByOauthType(oauthType2, oauthId2); // 解绑的账号
    if (map1 != null && !map1.isEmpty()) {
      userEntity1 = (UserEntity) map1.get("userEntity");
    }
    if (map2 != null && !map2.isEmpty()) {
      type = (String) map2.get("type");
      userEntity2 = (UserEntity) map2.get("userEntity");
    }
    if (userEntity1 != null && !userEntity1.getDelete()) {
      if (userEntity2 != null) {
        try {
          userService.unBingOthers(type, userEntity1, userEntity2);
          resultVO.setCode(0);
          resultVO.setMessage("解绑成功");
          return resultVO;
        } catch (RuntimeException e) {
          log.error("解绑授权授权账号捕获到异常:{}", e.getMessage());
        }
      }
    } else {
      resultVO.setCode(0);
      resultVO.setMessage("主动绑定账号不存在");
    }
    return resultVO;
  }
}
