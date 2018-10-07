package com.warpfuture.iot.oauth.controller;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.UserEntity;
import com.warpfuture.iot.oauth.exception.CreateFailException;
import com.warpfuture.iot.oauth.exception.DeleteFailException;
import com.warpfuture.iot.oauth.exception.QueryFailException;
import com.warpfuture.iot.oauth.exception.UpdateFailException;
import com.warpfuture.iot.oauth.service.UserService;
import com.warpfuture.iot.oauth.util.CommonUtil;
import com.warpfuture.iot.oauth.util.UUIDUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** Created by 徐海瀚 on 2018/4/20. */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

  @Autowired private UserService userService;

  /**
   * 创建用户
   *
   * @param userEntity
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public ResultVO<UserEntity> createUser(@RequestBody UserEntity userEntity) {
    ResultVO<UserEntity> resultVO = new ResultVO<>();
    String email = userEntity.getEmail();
    String phone = userEntity.getPhone();
    if (email != null && phone != null) {
      Criteria criteria = new Criteria();
      criteria.orOperator(Criteria.where("email").is(email), Criteria.where("phone").is(phone));
      Query query = new Query(criteria);
      try {
        UserEntity userEntity1 = userService.findOne(query);
        if (userEntity1 == null) {
          userEntity.setUserId(UUIDUtil.get12UUID());
          userEntity.setDelete(false);
          userEntity.setCreateTime(System.currentTimeMillis());
          userService.insert(userEntity);
          resultVO.setCode(1);
          resultVO.setMessage("创建用户成功");
          resultVO.setData(userEntity);
        } else if (!userEntity.getDelete()) {
          resultVO.setCode(0);
          resultVO.setMessage("该用户已存在");
        }
      } catch (CreateFailException e) {
        log.error("创建用户时捕获到异常:{}", e.getMessage());
      }
    } else {
      resultVO.setCode(0);
      resultVO.setMessage("邮箱账号和密码不能为空");
    }
    return resultVO;
  }

  /**
   * 更新用户
   *
   * @param userEntity
   * @return
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public ResultVO<UserEntity> updateUser(@RequestBody UserEntity userEntity) {
    ResultVO<UserEntity> resultVO = new ResultVO<>();
    String userId = userEntity.getUserId();
    if (userEntity != null) {
      try {
        UserEntity userEntity1 = userService.findById(userId); // 查询是否存在
        if (userEntity1 != null) {
          userService.save(userEntity);
        }
        resultVO.setCode(1);
        resultVO.setMessage("更新用户成功");
        resultVO.setData(userEntity);
        return resultVO;
      } catch (UpdateFailException e) {
        log.error("更新用户失败:{}", e.getMessage());
      }
    }
    resultVO.setCode(0);
    resultVO.setMessage("更新用户失败");
    return resultVO;
  }

  /**
   * 删除用户
   *
   * @param userId 用户id
   * @return
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResultVO<UserEntity> deleteUser(@NonNull @RequestParam("userId") String userId) {
    ResultVO<UserEntity> resultVO = new ResultVO<>();
    try {
      UserEntity userEntity = userService.findById(userId);
      if (userEntity != null) {
        userEntity.setDelete(true);
        userEntity.setUpdateTime(System.currentTimeMillis());
        userService.save(userEntity);
      }
    } catch (DeleteFailException e) {
      log.error("删除用户失败:{}", e.getMessage());
    }
    resultVO.setCode(0);
    resultVO.setMessage("删除用户失败");
    return resultVO;
  }

  /**
   * 查询用户
   *
   * @param userEntity
   * @return
   */
  @RequestMapping(value = "/query")
  public ResultVO<UserEntity> queryUser(@RequestBody UserEntity userEntity) {
    ResultVO<UserEntity> resultVO = new ResultVO<>();
    String userId = userEntity.getUserId();
    try {
      if (userId != null) {
        UserEntity userEntity1 = userService.findById(userId);
        if (userEntity1 != null) {
          resultVO.setCode(1);
          resultVO.setMessage("查询成功");
          resultVO.setData(userEntity1);
          return resultVO;
        }
      }
    } catch (QueryFailException e) {
      log.error("查询用户异常:{}", e.getMessage());
    }
    resultVO.setCode(0);
    resultVO.setMessage("查询用户失败");
    return resultVO;
  }

  /**
   * 分页查询
   *
   * @param accountId 企业id
   * @param applicationId 应用id
   * @param pageIndex 当前页
   * @param pageSize 每页显示数目大小
   * @return
   */
  @RequestMapping(value = "/list", method = RequestMethod.POST)
  public ResultVO<PageModel<UserEntity>> findAll(
      @NonNull @RequestParam("accountId") String accountId,
      @NonNull @RequestParam("applicationId") String applicationId,
      @NonNull @RequestParam("pageIndex") Integer pageIndex,
      @NonNull @RequestParam("pageSize") Integer pageSize) {
    ResultVO<PageModel<UserEntity>> resultVO = new ResultVO<>();
    try {
      PageModel<UserEntity> pageModel = new PageModel<>();
      pageModel.setPageSize(pageSize);
      pageModel.setPageIndex(pageIndex);
      Criteria criteria = new Criteria();
      criteria.andOperator(
          Criteria.where("accountId").is(accountId),
          Criteria.where("applicationId").is(applicationId));
      Query query = new Query(criteria);
      List<UserEntity> list = userService.find(query);
      if (list != null && !list.isEmpty()) {
        pageModel.setRowCount(list.size());
      }
      pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
      query.skip(pageModel.getSkip()).limit(pageSize);
      List<UserEntity> result = userService.find(query);
      pageModel.setData(result);
      resultVO.setCode(1);
      resultVO.setMessage("批量查询用户成功");
      resultVO.setData(pageModel); //
      return resultVO;
    } catch (QueryFailException e) {
      log.error("批量查询用户列表失败:{}", e.getMessage());
    }
    resultVO.setCode(0);
    resultVO.setMessage("批量查询不成功");
    return resultVO;
  }

  /**
   * 更新用户基本信息
   *
   * @param userEntity
   * @return
   */
  @RequestMapping(value = "/update/basic", method = RequestMethod.POST)
  public ResultVO<UserEntity> updateBasic(@RequestBody UserEntity userEntity) {
    ResultVO<UserEntity> resultVO = new ResultVO<>();
    if (userEntity != null) {
      String userId = userEntity.getUserId();
      String nickname = userEntity.getNickname();
      String phone = userEntity.getPhone();
      String email = userEntity.getEmail();
      if (userId != null) {
        try {
          UserEntity user = userService.findOne(new Query(Criteria.where("userId").is(userId)));
          if (user != null && !user.getDelete()) {
            user.setNickname(nickname);
            user.setPhone(phone);
            user.setEmail(email);
            user.setUpdateTime(System.currentTimeMillis());
            userService.save(user);
            resultVO.setCode(1);
            resultVO.setMessage("用户基本信息更新成功");
            resultVO.setData(user);
            return resultVO;
          }
        } catch (UpdateFailException e) {
          log.error("捕获到更新用户基本信息异常:{}", e.getMessage());
        }
      }
    }
    resultVO.setCode(0);
    resultVO.setMessage("用户基本信息更新失败");
    return resultVO;
  }

  /**
   * 更新用户拓展字段
   *
   * @param userEntity
   * @return
   */
  @RequestMapping(value = "/update/extension", method = RequestMethod.POST)
  public ResultVO updateExtension(@RequestBody UserEntity userEntity) {
    ResultVO resultVO = new ResultVO();
    if (userEntity != null) {
      String userId = userEntity.getUserId();
      Map<String, Object> extension = userEntity.getExpand();
      if (extension != null) {
        try {
          UserEntity user = userService.findOne(new Query(Criteria.where("userId").is(userId)));
          if (user != null && !user.getDelete()) {
            user.setExpand(extension);
            user.setUpdateTime(System.currentTimeMillis());
            userService.save(user);
            resultVO.setCode(1);
            resultVO.setMessage("用户拓展字段更新成功");
            return resultVO;
          }

        } catch (UpdateFailException e) {
          log.error("捕获到更新用户拓展字段异常:{}", e.getMessage());
        }
      }
    }
    resultVO.setCode(0);
    resultVO.setMessage("用户拓展字段更新失败");
    return resultVO;
  }

  /**
   * @param userAccount 登陆账号
   * @param password 登陆密码
   * @return
   */
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResultVO userLogin(
      @NonNull @RequestParam("userAccount") String userAccount,
      @NonNull @RequestParam("password") String password) {
    ResultVO<UserEntity> resultVO = new ResultVO<>();
    // 判断是否是邮箱账号
    UserEntity userEntity = null;
    if (CommonUtil.isEmail(userAccount)) {
      userEntity = userService.findOne(new Query(Criteria.where("email").is(userAccount)));
    } else if (CommonUtil.checkPhone(userAccount)) { // 判断是否是手机账号
      userEntity = userService.findOne(new Query(Criteria.where("phone").is(userAccount)));
    }
    if (userEntity != null) {
      if (password.equals(userEntity.getPassword())) {
        resultVO.setCode(1);
        resultVO.setMessage("登陆成功");
        resultVO.setData(userEntity);
      } else {
        resultVO.setCode(0);
        resultVO.setMessage("密码错误");
      }
    } else {
      resultVO.setCode(0);
      resultVO.setMessage("用户不存在");
    }
    return resultVO;
  }
}
