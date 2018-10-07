package com.warpfuture.iot.oauth.controller;

import com.warpfuture.entity.ApplicationEntity;
import com.warpfuture.entity.PageModel;
import com.warpfuture.iot.oauth.exception.CreateFailException;
import com.warpfuture.iot.oauth.exception.DeleteFailException;
import com.warpfuture.iot.oauth.exception.QueryFailException;
import com.warpfuture.iot.oauth.exception.UpdateFailException;
import com.warpfuture.iot.oauth.service.ApplicationService;
import com.warpfuture.iot.oauth.util.UUIDUtil;
import com.warpfuture.util.PageUtils;
import com.warpfuture.vo.ResultVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Created by 徐海瀚 on 2018/4/20. */
@RestController
@RequestMapping("/application")
@Slf4j
public class ApplicationController {
  @Autowired private ApplicationService applicationService;

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public ResultVO<ApplicationEntity> addApplication(
      @RequestBody ApplicationEntity applicationEntity) {
    String applicationName = applicationEntity.getApplicationName();
    ResultVO<ApplicationEntity> resultVO = new ResultVO<>();
    String applicationId = UUIDUtil.get12UUID();
    applicationEntity.setApplicationId(applicationId);
    applicationEntity.setCreateTime(System.currentTimeMillis());
    applicationEntity.setDelete(false);
    try {
      ApplicationEntity application =
          applicationService.findOne(
              new Query(Criteria.where("applicationName").is(applicationName)));
      if (application == null) {
        applicationService.insert(applicationEntity);
        resultVO.setCode(1);
        resultVO.setMessage("创建应用成功");
        resultVO.setData(applicationEntity);
      } else if (!application.getDelete()){
        resultVO.setCode(0);
        resultVO.setMessage("应用名称已存在");
      }
    } catch (CreateFailException e) {
      log.error("捕获到创建应用异常:{}", e.getMessage());
    }
    return resultVO;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResultVO deleteApplication(@NonNull @RequestParam("applicationId") String applicationId) {
    ResultVO resultVO = new ResultVO();
    try {
      ApplicationEntity applicationEntity =
          applicationService.findOne(
              new Query(Criteria.where("applicationId").is(applicationId)));
      if (applicationEntity != null) {
        if (applicationEntity.getDelete() != null && !applicationEntity.getDelete()) {
          applicationEntity.setDelete(true);
          applicationEntity.setUpdateTime(System.currentTimeMillis());
          applicationService.save(applicationEntity);
          resultVO.setCode(1);
          resultVO.setMessage("应用删除成功");
          return resultVO;
        }
      } else {
        resultVO.setCode(0);
        resultVO.setMessage("应用不存在，删除失败");
        return resultVO;
      }
    } catch (DeleteFailException e) {
      log.error("删除失败异常:{}", e.getMessage());
    }
    resultVO.setCode(0);
    resultVO.setMessage("应用删除失败");
    return resultVO;
  }

  @RequestMapping(value = "/update")
  public ResultVO updateApplication(@RequestBody ApplicationEntity applicationEntity) {
    ResultVO resultVO = new ResultVO();
    try {
      applicationService.save(applicationEntity);
    } catch (UpdateFailException e) {
      log.error("更新失败异常:{}", e.getMessage());
      resultVO.setCode(0);
      resultVO.setMessage(e.getMessage());
    }
    return resultVO;
  }

  @RequestMapping(value = "/query", method = RequestMethod.POST)
  public ResultVO<ApplicationEntity> find(@RequestBody ApplicationEntity applicationEntity) {
    ResultVO<ApplicationEntity> resultVO = new ResultVO<>();
    String applicationId = applicationEntity.getApplicationId();
    String accountId = applicationEntity.getAccountId();

    try {
      ApplicationEntity applicationEntity1 =
          applicationService.findById(applicationId);
      if (applicationEntity1 != null && applicationEntity1.getDelete() != true) {
        if (accountId.equals(applicationEntity1.getAccountId())) {
          resultVO.setCode(1);
          resultVO.setMessage("查询成功");
          resultVO.setData(applicationEntity1);
        } else {
          resultVO.setCode(0);
          resultVO.setMessage("应用不存在");
        }
      }
    } catch (QueryFailException e) {
      log.error("查询失败异常:{}", e.getMessage());
      resultVO.setCode(0);
      resultVO.setMessage(e.getMessage());
      resultVO.setData(null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  public ResultVO<PageModel<ApplicationEntity>> findAll(
      @NonNull @RequestParam("accountId") String accountId,
      @NonNull @RequestParam("pageIndex") Integer pageIndex,
      @NonNull @RequestParam("pageSize") Integer pageSize) {
    ResultVO<PageModel<ApplicationEntity>> resultVO = new ResultVO<>();
    try {
      PageModel pageModel = new PageModel();
      pageModel.setPageIndex(pageIndex);
      pageModel.setPageSize(pageSize);
      Criteria criteria = new Criteria();
      criteria.andOperator(
          Criteria.where("accountId").is(accountId), Criteria.where("isDelete").is(false));
      Query query = new Query(criteria);
      List<ApplicationEntity> list = applicationService.find(query);
      long rowCount = 0;
      if (list != null && !list.isEmpty()) {
        rowCount = list.size();
      }
      pageModel.setRowCount((int) rowCount);
      //pageModel=PageUtils.dealPage(pageModel);
      pageModel.setSkip(PageUtils.getSkip(pageIndex, pageSize));
      query.skip(pageModel.getSkip()).limit(pageSize);
      List<ApplicationEntity> result = applicationService.find(query);
      pageModel.setData(result);
      resultVO.setCode(1);
      resultVO.setMessage("批量查询应用成功");
      resultVO.setData(pageModel);
      return resultVO;
    } catch (QueryFailException e) {
      log.error("获取批量应用失败{}", e.getMessage());
    }
    resultVO.setCode(0);
    resultVO.setMessage("应用批量获取失败");
    return resultVO;
  }

  @RequestMapping(value = "/update/basic", method = RequestMethod.POST)
  public ResultVO updateBaseInformation(@RequestBody ApplicationEntity applicationEntity) {
    ResultVO resultVO = new ResultVO();
    String accountId = applicationEntity.getAccountId();
    String applicationId = applicationEntity.getApplicationId();
    String applicationName = applicationEntity.getApplicationName();
    String applicationDescribe = applicationEntity.getApplicationDescribe();
    if (accountId != null && applicationId != null) {
      Criteria criteria = new Criteria();
      criteria.andOperator(
          Criteria.where("applicationId").is(applicationId),
          Criteria.where("accountId").is(accountId));
      Query query = new Query(criteria);
      try {
        ApplicationEntity applicationEntity1 =
            applicationService.findOne(query);
        if (applicationEntity1 != null && !applicationEntity1.getDelete()) {
          applicationEntity1.setApplicationName(applicationName);
          applicationEntity1.setApplicationDescribe(applicationDescribe);
          applicationEntity1.setUpdateTime(System.currentTimeMillis());
          applicationService.save(applicationEntity1);
          resultVO.setCode(1);
          resultVO.setMessage("应用基本信息更新成功");
          return resultVO;
        }
      } catch (UpdateFailException e) {
        log.error("捕获到更新失败异常:{}", e.getMessage());
      }
    }
    resultVO.setCode(0);
    resultVO.setMessage("应用基本信息更新失败");
    return resultVO;
  }

  @RequestMapping(value = "/update/expansion", method = RequestMethod.POST)
  public ResultVO updateExpansion(@RequestBody ApplicationEntity applicationEntity) {
    ResultVO resultVO = new ResultVO();
    String accountId = applicationEntity.getAccountId();
    String applicationId = applicationEntity.getApplicationId();
    if (accountId != null && applicationId != null) {
      Criteria criteria = new Criteria();
      criteria.andOperator(
          Criteria.where("applicationId").is(applicationId),
          Criteria.where("accountId").is(accountId));
      Query query = new Query(criteria);
      try {
        ApplicationEntity applicationEntity1 =
            applicationService.findOne(query);
        if (applicationEntity1 != null && !applicationEntity1.getDelete()) {
          applicationEntity1.setExpand(applicationEntity.getExpand());
          applicationEntity1.setUpdateTime(System.currentTimeMillis());
          applicationService.save(applicationEntity1);
          resultVO.setCode(1);
          resultVO.setMessage("应用拓展字段更新成功");
          return resultVO;
        }
      } catch (UpdateFailException e) {
        log.error("捕获到更新失败异常:{}", e.getMessage());
      }
    }
    resultVO.setCode(0);
    resultVO.setMessage("应用拓展字段更新失败");
    return resultVO;
  }

  @RequestMapping(value = "/update/oauthways", method = RequestMethod.POST)
  public ResultVO updateOauthWays(@RequestBody ApplicationEntity applicationEntity) {
    ResultVO resultVO = new ResultVO();
    String accountId = applicationEntity.getAccountId();
    String applicationId = applicationEntity.getApplicationId();
    if (accountId != null && applicationId != null) {
      Criteria criteria = new Criteria();
      criteria.andOperator(
          Criteria.where("applicationId").is(applicationId),
          Criteria.where("accountId").is(accountId));
      Query query = new Query(criteria);
      try {
        ApplicationEntity applicationEntity1 =
            applicationService.findOne(query);
        if (applicationEntity1 != null && !applicationEntity1.getDelete()) {
          applicationEntity1.setOauthType(applicationEntity.getOauthType());
          applicationEntity1.setOauthData(applicationEntity.getOauthData());
          applicationEntity1.setUpdateTime(System.currentTimeMillis());
          applicationService.save(applicationEntity1);
          resultVO.setCode(1);
          resultVO.setMessage("应用认证方式更新成功");
          return resultVO;
        }
      } catch (UpdateFailException e) {
        log.error("捕获到更新失败异常:{}", e.getMessage());
      }
    }
    resultVO.setCode(0);
    resultVO.setMessage("应用认证方式更新失败");
    return resultVO;
  }
}
