package com.warpfuture.controller;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.order.Order;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.service.OrderService;
import com.warpfuture.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** Created by fido on 2018/5/15. */
@RequestMapping("/order")
@RestController
public class OrderController {

  private Logger logger = LoggerFactory.getLogger(OrderController.class);
  @Autowired private OrderService orderService;

  @RequestMapping(value = "/query", method = RequestMethod.POST)
  public ResultVO<Order> query(@RequestBody Order order) {
    String accountId = order.getAccountId();
    String merchantTradeNumber = order.getMerchantTradeNumber();
    String merchantId = order.getMerchantId();
    ResultVO<Order> resultVO = null;
    try {
      Order queryOrder = orderService.query(accountId, merchantTradeNumber, merchantId);
      if (queryOrder != null) {
        resultVO = new ResultVO(ResponseCode.ORDER_QUERY_SUCCESS.getCode(), "查询成功", queryOrder);
      } else resultVO = new ResultVO(ResponseCode.ORDER_QUERY_SUCCESS.getCode(), "无此订单");
    } catch (PermissionFailException e) {
      logger.debug("查询订单权限错误");
      resultVO = new ResultVO(ResponseCode.ORDER_QUERY_FAIL.getCode(), e.getMessage());
    } catch (Exception e) {
      logger.debug("查询订单异常");
      resultVO = new ResultVO(ResponseCode.ORDER_QUERY_FAIL.getCode(), "查询订单异常");
    }
    return resultVO;
  }

  @RequestMapping(value = "/queryOrderList", method = RequestMethod.POST)
  public ResultVO<PageModel<Order>> queryOrderList(
      @RequestBody Merchant merchant,
      @RequestParam Integer pageSize,
      @RequestParam Integer pageIndex) {
    String accountId = merchant.getAccountId();
    String merchantId = merchant.getMerchantId();
    ResultVO<PageModel<Order>> resultVO = null;
    try {
      PageModel<Order> pageModel =
          orderService.queryList(accountId, merchantId, pageSize, pageIndex);
      resultVO = new ResultVO(ResponseCode.ORDER_QUERY_SUCCESS.getCode(), "查询订单列表成功", pageModel);
    } catch (PermissionFailException e) {
      logger.debug("查询订单列表权限错误");
      resultVO = new ResultVO(ResponseCode.ORDER_QUERY_FAIL.getCode(), e.getMessage());
    } catch (Exception e) {
      logger.debug("查询订单列表异常");
      resultVO = new ResultVO(ResponseCode.ORDER_QUERY_FAIL.getCode(), "查询订单列表异常");
    }
    return resultVO;
  }
}
