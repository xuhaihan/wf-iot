package com.warpfuture.controller;

import com.alibaba.fastjson.JSONObject;
import com.warpfuture.constant.Constant;
import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.Production;
import com.warpfuture.exception.ParameterIllegalException;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.ProductionService;
import com.warpfuture.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** Created by fido on 2018/4/16. */
@RestController
@RequestMapping("/production")
public class ProductionController {
  @Autowired private ProductionService productionService;

  private Logger logger = LoggerFactory.getLogger(ProductionController.class);

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public ResultVO<Production> create(@RequestBody Production production) {
    ResultVO resultVO = null;
    logger.info("---" + "创建产品：---" + production);
    try {
      Production result = productionService.createProduction(production);
      resultVO = new ResultVO(Constant.SUCCESS, "插入成功", production);
    } catch (ParameterIllegalException e) {
      logger.error("插入产品有误--" + e.getMessage());
      resultVO = new ResultVO(Constant.SUCCESS, e.getMessage());
    } catch (Exception e) {
      logger.error("插入产品异常--" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "插入失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/account/query", method = RequestMethod.POST)
  public ResultVO<PageModel<Production>> queryByAccountId(
      @RequestParam(value = "accountId") String accountId,
      @RequestParam Integer pageSize,
      @RequestParam Integer pageIndex) {
    PageModel<Production> result =
        productionService.findByAccountId(accountId, pageSize, pageIndex);
    ResultVO resultVO = new ResultVO(Constant.SUCCESS, "查询成功", result);
    return resultVO;
  }

  @RequestMapping(value = "/query", method = RequestMethod.POST)
  public ResultVO<Production> query(@RequestBody Production production) {
    ResultVO resultVO = null;
    String productionId = production.getProductionId();
    String accountId = production.getAccountId();
    try {
      Production findProduction = productionService.findById(productionId, accountId);
      resultVO = new ResultVO(Constant.SUCCESS, "查询成功", findProduction);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResultVO delete(@RequestBody Production production) {
    ResultVO resultVO = null;
    try {
      String productionId = production.getProductionId();
      String accountId = production.getAccountId();
      productionService.deleteProduction(productionId, accountId);
      resultVO = new ResultVO(Constant.SUCCESS, "删除成功", null);
    } catch (PermissionFailException e) {
      logger.error("权限错误");
      resultVO = new ResultVO(Constant.FAIL, e.getMessage(), null);
    } catch (Exception e) {
      logger.error("删除异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "删除失败", null);
    }
    return resultVO;
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public ResultVO<Production> update(@RequestBody Production production) {
    ResultVO resultVO = null;
    try {
      logger.info("--更新产品--：" + production);
      Production newProduction = productionService.updateProductionInfo(production);
      resultVO = new ResultVO(Constant.SUCCESS, "更新成功", newProduction);
    } catch (PermissionFailException e) {
      logger.error(e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, e.getMessage());
    } catch (ParameterIllegalException e) {
      logger.error("更新产品有误：--" + e.getMessage());
    } catch (Exception e) {
      logger.error("更新异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "更新失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/down", method = RequestMethod.POST)
  public ResultVO<Production> revoke(@RequestBody Production queryProduction) {
    ResultVO resultVO = null;
    try {
      String accountId = queryProduction.getAccountId();
      String productionId = queryProduction.getProductionId();
      Production production = productionService.revokeProduction(productionId, accountId);
      resultVO = new ResultVO(Constant.SUCCESS, "下架成功", production);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage());
    } catch (Exception e) {
      logger.error("下架异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "下架失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/publish", method = RequestMethod.POST)
  public ResultVO<Production> publish(@RequestBody Production production) {
    ResultVO resultVO = null;
    try {
      String productionId = production.getProductionId();
      String accountId = production.getAccountId();
      Production newProduction = productionService.publishProduction(productionId, accountId);
      resultVO = new ResultVO(Constant.SUCCESS, "发布成功", newProduction);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage());
    } catch (Exception e) {
      logger.error("发布异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "发布失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/updateExtensions", method = RequestMethod.POST)
  public ResultVO<Production> updateExtensions(@RequestBody Production production) {
    ResultVO resultVO = null;
    try {
      Production newProduction = productionService.updateExtensions(production);
      resultVO = new ResultVO(Constant.SUCCESS, "更改拓展字段成功", newProduction);
    } catch (PermissionFailException e) {
      resultVO = new ResultVO(Constant.FAIL, e.getMessage());
    } catch (Exception e) {
      logger.error("更新拓展字段异常" + e.getMessage());
      resultVO = new ResultVO(Constant.FAIL, "更改拓展字段失败");
    }
    return resultVO;
  }
}
