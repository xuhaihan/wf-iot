package com.warpfuture.controller;

import com.warpfuture.dto.OrderOperationDto;
import com.warpfuture.entity.order.Order;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.exception.*;
import com.warpfuture.service.WxPayService;
import com.warpfuture.util.PayWxUtils;
import com.warpfuture.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Created by fido on 2018/5/17. */
@RequestMapping("/wxpay")
@RestController
public class WxPayController {
  @Autowired private WxPayService wxPayService;

  private Logger logger = LoggerFactory.getLogger(WxPayController.class);

  @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
  public ResultVO<Map<String, String>> create(@RequestBody OrderOperationDto orderOperationDto) {
    ResultVO<Map<String, String>> resultVO = null;
    try {
      // 得到调用统一下单接口返回的给业务服务器的数据包
      Map<String, String> preparPackage = wxPayService.preparCreateOrder(orderOperationDto);
      if (preparPackage != null) {
        resultVO =
            new ResultVO<Map<String, String>>(
                ResponseCode.WXPAY_OPERATION_SUCCESS.getCode(), "下单成功", preparPackage);
      } else {
        resultVO =
            new ResultVO<Map<String, String>>(
                ResponseCode.WXPAY_OPERATION_FAIL.getCode(), "支付类型有错");
      }
    } catch (PermissionFailException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (ParamsErrorException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (WxPayReturnException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (WxPayResultException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (SignErroException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("下单异常");
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), "下单失败");
    }
    return resultVO;
  }

  @RequestMapping(value = "/callBack", method = RequestMethod.POST)
  public String callBack(@RequestBody String returnPackage) {
    String result = null;
    try {
      result = wxPayService.notifyPayment(returnPackage);
    } catch (DataNotLegalException e) {
      logger.info("==接收微信服务器回调时数据不匹配==" + e.getMessage());
      result = PayWxUtils.returnFail();
    } catch (Exception e) {
      logger.info("==接收微信服务器回调时发生异常==" + e.getMessage());
      result = PayWxUtils.returnFail();
    }
    return result;
  }

  @RequestMapping(value = "/refundBack", method = RequestMethod.POST)
  public String refundBack(@RequestBody String returnPackage) {
    String result = null;
    try {
      result = wxPayService.notifyRefund(returnPackage);
    } catch (DataNotLegalException e) {
      logger.info("==接收微信服务器回调时数据不匹配==" + e.getMessage());
      result = PayWxUtils.returnFail();
    } catch (Exception e) {
      logger.info("==接收微信服务器回调时发生异常==" + e.getMessage());
      result = PayWxUtils.returnFail();
    }
    return result;
  }

  @RequestMapping(value = "/createRefund", method = RequestMethod.POST)
  public ResultVO<Order> createRefund(@RequestBody OrderOperationDto orderOperationDto) {
    ResultVO<Order> resultVO = null;
    try {
      // 得到调用统一下单接口返回的给业务服务器的数据包
      Order order = wxPayService.refund(orderOperationDto);
      if (order != null) {
        resultVO =
            new ResultVO<>(ResponseCode.WXPAY_OPERATION_SUCCESS.getCode(), "提交退款申请成功", order);
      }
    } catch (PermissionFailException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (ParamsErrorException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (WxPayReturnException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (WxPayResultException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (SignErroException e) {
      logger.info(e.getMessage());
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("提交退款申请异常");
      resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), "提交退款申请失败");
    }
    return resultVO;
  }
}
