package com.warpfuture.controller;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.exception.ParameterIllegalException;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.MerchantService;
import com.warpfuture.vo.ResultVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Created by fido on 2018/4/17. */
@RestController
@RequestMapping("/merchant")
@Log4j2
public class MerchantController {
  @Autowired private MerchantService merchantService;

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public ResultVO<Merchant> create(@RequestBody Merchant merchant) {
    ResultVO resultVO = null;
    log.info("==插入的商户信息==" + merchant);
    try {
      Merchant createMerchant = merchantService.create(merchant);
      if (createMerchant != null) {
        resultVO =
            new ResultVO(
                ResponseCode.MERCHANT_OPERATION_SUCCESS.getCode(),
                "create merchant success",
                createMerchant);
      } else
        resultVO =
            new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), "create merchant fail");
    } catch (ParameterIllegalException e) {
      log.info("创建商户" + e.getMessage());
      resultVO = new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      resultVO =
          new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), "create merchant fail");
    }
    return resultVO;
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public ResultVO<Merchant> update(@RequestBody Merchant merchant) {
    ResultVO resultVO = null;
    try {
      Merchant result = merchantService.update(merchant);
      if (result != null)
        resultVO =
            new ResultVO(
                ResponseCode.MERCHANT_OPERATION_SUCCESS.getCode(),
                "update merchant success",
                result);
      else
        resultVO =
            new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), "update merchant fail");
    } catch (PermissionFailException e) {
      log.info("==更新商户失败==" + e.getMessage());
      resultVO = new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (Exception e) {
      log.info("==更新商户异常==" + e.getMessage());
      resultVO =
          new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), "update merchant fail");
    }
    return resultVO;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResultVO delete(@RequestBody Merchant merchant) {
    ResultVO resultVO = null;
    try {
      String merchantId = merchant.getMerchantId();
      String accountId = merchant.getAccountId();
      merchantService.delete(accountId, merchantId);
      resultVO =
          new ResultVO(
              ResponseCode.MERCHANT_OPERATION_SUCCESS.getCode(), "delete merchant success");
    } catch (PermissionFailException e) {
      log.info("==s删除商户失败==" + e.getMessage());
      resultVO = new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (Exception e) {
      resultVO =
          new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), "delete merchant fail");
    }
    return resultVO;
  }

  @RequestMapping(value = "/query", method = RequestMethod.POST)
  public ResultVO<Merchant> findById(@RequestBody Merchant merchant) {
    ResultVO<Merchant> resultVO = null;
    try {
      String accountId = merchant.getAccountId();
      String merchantId = merchant.getMerchantId();
      Merchant queryMerchant = merchantService.findById(accountId, merchantId);
      resultVO =
          new ResultVO(
              ResponseCode.MERCHANT_OPERATION_SUCCESS.getCode(),
              "find merchant success",
              queryMerchant);
    } catch (PermissionFailException e) {
      log.info("==查找商户失败==" + e.getMessage());
      resultVO = new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (Exception e) {
      resultVO = new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), "find merchant fail");
    }
    return resultVO;
  }

  @RequestMapping(value = "/queryByAccount", method = RequestMethod.POST)
  public ResultVO<List<Merchant>> findByAccountId(
      @RequestParam String accountId,
      @RequestParam Integer pageIndex,
      @RequestParam Integer pageSize) {
    ResultVO<List<Merchant>> resultVO = null;
    try {
      PageModel<Merchant> merchantList =
          merchantService.findByAccountId(accountId, pageIndex, pageSize);
      resultVO =
          new ResultVO(
              ResponseCode.MERCHANT_OPERATION_SUCCESS.getCode(),
              "find merchant list success",
              merchantList);
    } catch (Exception e) {
      e.printStackTrace();
      resultVO =
          new ResultVO(ResponseCode.MERCHANT_OPERATION_FAIL.getCode(), "find merchant list fail");
    }
    return resultVO;
  }
}
