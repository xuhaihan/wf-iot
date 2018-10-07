package com.warpfuture.service.impl;

import com.google.gson.Gson;
import com.warpfuture.constant.OrderConstant;
import com.warpfuture.constant.WxPayConstant;
import com.warpfuture.dto.NotifyStreamInfoDto;
import com.warpfuture.dto.OrderOperationDto;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.merchant.WxpayData;
import com.warpfuture.entity.order.Order;
import com.warpfuture.entity.order.WxOrderData;
import com.warpfuture.exception.*;
import com.warpfuture.repository.MerchantRepository;
import com.warpfuture.repository.OrderRepository;
import com.warpfuture.service.WxPayService;
import com.warpfuture.stream.NotifyProducter;
import com.warpfuture.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/** Created by fido on 2018/5/16. */
@Service
public class WxPayServiceImpl implements WxPayService {

  @Autowired private MerchantRepository merchantRepository;
  @Autowired private OrderRepository orderRepository;
  @Autowired private RestTemplate restTemplate;
  @Autowired private NotifyProducter notifyProducter; // kafka生产者
  @Autowired private ClientCustomSSL clientCustomSSL;

  private Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);

  @Override
  public Map<String, String> preparCreateOrder(OrderOperationDto orderOperationDto) {
    long start = System.currentTimeMillis(); // 用于计算运行时间
    logger.info("--请求微信支付下单接口获取的信息--：" + orderOperationDto);
    String accountId = orderOperationDto.getAccountId();
    String merchantId = orderOperationDto.getMerchantId();
    Map<String, String> params = orderOperationDto.getParams(); // 传过来的参数
    Merchant merchant = merchantRepository.findById(merchantId);
    // 找不到商户
    if (merchant == null) {
      throw new PermissionFailException("找不到商户");
    }
    // 是否有操作的权限
    boolean permission = PermissionVertifyUtils.getPayPermission(accountId, merchant);
    if (!permission) {
      throw new PermissionFailException("权限错误");
    }
    // 通过以上权限验证，才有权利发起本次支付请求
    // 获得相关的微信支付配置
    WxpayData wxPayData = merchant.getWxPayData();
    logger.info("微信相关的配置" + wxPayData);
    String body = params.get("body"); // 商品描述
    String merchantTradeNumber = params.get("out_trade_no"); // 商户订单号
    String totalAmount = params.get("total_fee"); // 标价金额
    String ip = PayWxUtils.getLocalIP(); // 获得本机IP
    String tradeType = params.get("trade_type"); // 交易类型
    if (StringUtils.isBlank(tradeType)) {
      throw new ParamsErrorException("交易类型不能为空");
    }
    String openId = params.get("openid"); // 用户标识
    // JS公众号支付
    if (WxPayConstant.WX_PAYTYPE_JS.equals(tradeType)) {
      if (StringUtils.isBlank(body)) {
        throw new ParamsErrorException("商品描述不能为空");
      }
      if (StringUtils.isBlank(merchantTradeNumber)) {
        throw new ParamsErrorException("商户订单号不能为空");
      }
      if (StringUtils.isBlank(totalAmount)) {
        throw new ParamsErrorException("标价金额不能为空");
      }
      if (!StringUtils.isNumeric(totalAmount)) {
        throw new ParamsErrorException("标价金额格式错误");
      }
      if (StringUtils.isBlank(ip)) {
        throw new ParamsErrorException("终端IP不能为空");
      }
      if (StringUtils.isBlank(openId)) {
        throw new ParamsErrorException("用户openid不能为空");
      }
      Order originOrder =
          orderRepository.findByMerchantTradeNumber(merchantId, merchantTradeNumber);
      if (originOrder != null) {
        throw new ParamsErrorException("订单已存在");
      }
      // 开始进行数据签名
      String wxPayMerchantId = wxPayData.getWxPayMerchantId(); // 获得商户号
      params.put("mch_id", wxPayMerchantId);
      params.put("appid", wxPayData.getWxPayAppId());
      String nonce_str = PayWxUtils.getNonceStr(); // 随机字符串
      params.put("nonce_str", nonce_str);
      params.put("spbill_create_ip", ip);
      params.put("notify_url", WxPayConstant.dataNotifyURL);
      String signPackage = WxpayJSAPIUtils.getSignPackage(wxPayData, params); // 获得签名
      logger.info("==预下单请求的xml==" + signPackage);
      // 提交预下单请求，获得微信支付服务器返回的结果
      ResponseEntity<String> responseStr =
          restTemplate.postForEntity(WxPayConstant.wxCreateOrderURL, signPackage, String.class);
      String str = responseStr.getBody(); // 获得请求下单结果
      Map<String, String> resultMap = XMLUtils.xmlStr2Map(str);
      String returnCode = resultMap.get("return_code");
      String resultCode = resultMap.get("result_code");
      if (!WxPayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
        try {
          logger.info("与微信服务器通信标识错误===" + new String(resultMap.get("return_msg")));
        } catch (Exception e) {
          throw new WxPayReturnException("与微信服务器通信标识错误");
        }
      }
      if (!WxPayConstant.RESULT_CODE_SUCCESS.equals(resultCode)) {
        try {
          logger.info("err_code===" + new String(resultMap.get("err_code")));
        } catch (Exception e) {
          throw new WxPayResultException("交易标识错误");
        }
      }
      // 开始验签
      boolean verifySign = PayWxUtils.verfitySign(resultMap, wxPayData.getWxPaySignKey());
      if (!verifySign) {
        throw new SignErroException("得到微信服务器签名校验错误");
      }
      // 签名通过，需要返回信息给业务服务器
      Map<String, String> return2JS = WxpayJSAPIUtils.getReturn2JSAPI(resultMap, wxPayData);
      // 创建初始订单
      Order order = new Order();
      order.setAccountId(accountId);
      order.setMerchantId(merchantId);
      String orderId = IdUtils.getId();
      logger.info("创建的订单id：" + orderId);
      order.setOrderId(orderId);
      order.setStatus(OrderConstant.ORDER_STATUS_READY); // 未支付
      order.setMerchantTradeNumber(merchantTradeNumber);
      order.setTradeType(OrderConstant.ORDER_WXTRADE); // 微信支付
      WxOrderData wxOrderData = new WxOrderData();
      Map<String, Object> extensions = new HashMap<>(); // 一些微信订单的特有参数
      wxOrderData.setWxPayTradeType(tradeType);
      wxOrderData.setTotalAmount(Integer.valueOf(totalAmount));
      extensions.put("body", body);
      extensions.put("ip", ip);
      wxOrderData.setExtensions(extensions);
      order.setCreateTime(System.currentTimeMillis());
      order.setUpdateTime(System.currentTimeMillis());
      order.setOrderData(wxOrderData);
      orderRepository.insert(order);
      logger.info("--支付接口签名信息--：" + signPackage);
      long end = System.currentTimeMillis();
      logger.info("--支付签名用时--：" + (end - start) + "ms");
      return return2JS;
    }
    return null;
  }

  @Override
  public String notifyPayment(String returnPackage) {
    logger.info("==接收到的支付结果回调通知为：==" + returnPackage);
    Map<String, String> returnMap = XMLUtils.xmlStr2Map(returnPackage);
    NotifyStreamInfoDto notifyStreamInfoDto = new NotifyStreamInfoDto(); // 丢进kafka的消息对象
    notifyStreamInfoDto.setNums(1); // 第一次发送
    notifyStreamInfoDto.setTradeType(OrderConstant.ORDER_WXTRADE);
    notifyStreamInfoDto.setNotifyType(OrderConstant.NOTIFY_UNIFIEDORDER); // 下单回调
    notifyStreamInfoDto.setTime(System.currentTimeMillis()); // 发送时间设为当前时间
    String returnCode = returnMap.get("return_code"); // 微信支付服务器返回的状态码
    // TODO   判断通信结果，若为fail,没有商家信息返回
    if (!WxPayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
      logger.info("==微信支付服务器回调retrun_code为fail==" + returnMap.get("return_msg"));
      return PayWxUtils.returnFail();
    }
    String resultCode = returnMap.get("result_code");
    String appId = returnMap.get("appid");
    String wxPayMerchantId = returnMap.get("mch_id"); // 商户号
    Merchant merchant = merchantRepository.findByAppIdAndMchId(appId, wxPayMerchantId);
    notifyStreamInfoDto.setMerchant(merchant);
    // 判断业务结果
    if (!WxPayConstant.RESULT_CODE_SUCCESS.equals(resultCode)) {
      String errMesg = returnMap.get("err_code"); // 返回的错误信息
      notifyStreamInfoDto.setResult(0);
      notifyStreamInfoDto.setMesg(errMesg);
      return PayWxUtils.returnFail();
    }
    String wxSign = returnMap.get("sign"); // 获得微信回调的签名
    Integer totalAmount = Integer.valueOf(returnMap.get("total_fee")); // 总金额
    String merchantTradeNumber = returnMap.get("out_trade_no"); // 商户订单号
    // 商户不对应
    if (merchant == null) {
      throw new DataNotLegalException("商户不对应");
    }
    Order order =
        orderRepository.findByMerchantTradeNumber(merchant.getMerchantId(), merchantTradeNumber);
    if (order == null) {
      throw new DataNotLegalException("订单不存在");
    }
    String merchantId = order.getMerchantId(); // 获得商户Id
    // 已经支付成功了
    if (order.getStatus() == OrderConstant.ORDER_STATUS_PAY) {
      return PayWxUtils.returnSuccess();
    }
    // 进行验签操作
    WxpayData wxPayData = merchant.getWxPayData();
    boolean vertifySign = PayWxUtils.verfitySign(returnMap, wxPayData.getWxPaySignKey());
    if (!vertifySign) {
      throw new DataNotLegalException("签名不匹配");
    }
    Integer originTotalAmount = order.getOrderData().getTotalAmount(); // 金额
    if (originTotalAmount != totalAmount) {
      throw new DataNotLegalException("金额不匹配");
    }
    String endTime = returnMap.get("time_end"); // 支付完成时间
    // 验证通过，更改订单状态,回调给业务服务器支付结果
    order.setStatus(OrderConstant.ORDER_STATUS_PAY); // 已支付
    order.setUpdateTime(DateUtils.getTime(endTime)); // 更新时间
    WxOrderData wxOrderData = (WxOrderData) order.getOrderData();
    Map<String, Object> extensions = wxOrderData.getExtensions();
    extensions.put("bankType", returnMap.get("bank_type")); // 付款银行
    wxOrderData.setExtensions(extensions);
    wxOrderData.setWxOpenId(returnMap.get("openid"));
    wxOrderData.setWxTradeNumber(returnMap.get("transaction_id"));
    order.setOrderData(wxOrderData);
    orderRepository.save(order);
    notifyStreamInfoDto.setOrder(
        orderRepository.findByMerchantTradeNumber(merchantId, merchantTradeNumber));
    notifyStreamInfoDto.setResult(1);
    // 丢进kafka里
    Gson gson = new Gson();
    String json = gson.toJson(notifyStreamInfoDto);
    notifyProducter.sendNotifyMsg(json);
    return PayWxUtils.returnSuccess();
  }

  @Override
  public String prepareQueryData(String merchantId, String merchantTradeNumber) {
    Merchant merchant = merchantRepository.findById(merchantId);
    WxpayData wxpayData = merchant.getWxPayData();
    String appId = wxpayData.getWxPayAppId();
    String wxMerchantIdNumber = wxpayData.getWxPayMerchantId();
    String tradeNumber = merchantTradeNumber;
    Map<String, String> map = new HashMap();
    map.put("appid", appId);
    map.put("mch_id", wxMerchantIdNumber);
    map.put("out_trade_no", tradeNumber);
    String nonceStr = PayWxUtils.getNonceStr(); // 随机字符串
    map.put("nonce_str", nonceStr);
    String sign = PayWxUtils.getMD5Sign(map, wxpayData.getWxPaySignKey());
    map.put("sign", sign);
    String packageStr = XMLUtils.map2str(map);
    return packageStr;
  }

  @Override
  public Order refund(OrderOperationDto orderOperationDto) {
    long start = System.currentTimeMillis(); // 用于计算运行时间
    logger.info("--请求退款接口获取的信息--：" + orderOperationDto);
    String accountId = orderOperationDto.getAccountId();
    String merchantId = orderOperationDto.getMerchantId();
    Map<String, String> params = orderOperationDto.getParams(); // 传过来的参数
    Merchant merchant = merchantRepository.findById(merchantId);
    // 找不到商户
    if (merchant == null) {
      throw new PermissionFailException("找不到商户");
    }
    // 是否有操作的权限
    boolean permission = PermissionVertifyUtils.getPayPermission(accountId, merchant);
    if (!permission) {
      throw new PermissionFailException("权限错误");
    }
    String refundAmound = params.get("refund_fee"); // 退款金额

    String merchantTradeNumber = params.get("out_trade_no"); // 获得订单号
    if (StringUtils.isBlank(merchantTradeNumber)) {
      throw new ParamsErrorException("商户订单号不能为空");
    }
    if (StringUtils.isBlank(refundAmound)) {
      throw new ParamsErrorException("退款金额不能为空");
    }
    if (!StringUtils.isNumeric(refundAmound)) {
      throw new ParamsErrorException("退款金额格式错误");
    }
    // 通过以上权限验证，才有权利发起本次退款请求
    Order order = orderRepository.findByMerchantTradeNumber(merchantId, merchantTradeNumber);
    WxOrderData wxOrderData = (WxOrderData) order.getOrderData();
    String wxTradeNumber = wxOrderData.getWxTradeNumber(); // 微信订单号
    // 获得相关的微信支付配置
    WxpayData wxPayData = merchant.getWxPayData();
    String mch_id = wxPayData.getWxPayMerchantId(); // 获得商户号
    params.put("mch_id", mch_id); // 商户号
    params.put("appid", wxPayData.getWxPayAppId()); // 公众账号
    String nonce_str = PayWxUtils.getNonceStr(); // 随机字符串
    logger.info("随机字符串为==" + nonce_str);
    params.put("nonce_str", nonce_str);
    params.put("refund_account", WxPayConstant.REFUND_ACCOUNT_RECHARGE); // 使用可用余额退款
    params.put("out_trade_no", merchantTradeNumber);
    params.put("total_fee", String.valueOf(wxOrderData.getTotalAmount())); // 订单总金额
    params.put("notify_url", WxPayConstant.refundNotifyURL); // 退款回调通知地址
    String sign = PayWxUtils.getMD5Sign(params, wxPayData.getWxPaySignKey());
    // 使用MD5加密算法进行加密签名
    logger.info("sign==" + sign);
    params.put("sign", sign);
    String signRefundPackage = XMLUtils.map2str(params);
    logger.info("请求退款的数据包" + signRefundPackage);
    String str = null;
    try {
      str =
          clientCustomSSL.doRefund(
              WxPayConstant.WX_REFUND_URL,
              signRefundPackage,
              wxPayData.getWxPayRefundBook(),
              wxPayData.getWxPayMerchantId()); // 获得请求退款结果
    } catch (Exception e) {
      e.printStackTrace();

      logger.info("退款证书建立失败");
    }
    logger.info("==得到微信支付退款回复==" + str);
    Map<String, String> resultMap = XMLUtils.xmlStr2Map(str);
    String returnCode = resultMap.get("return_code");
    String resultCode = resultMap.get("result_code");
    if (!WxPayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
      try {
        logger.info("与微信服务器通信标识错误===" + resultMap.get("return_msg"));
      } catch (Exception e) {
        throw new WxPayReturnException("与微信服务器通信标识错误");
      }
    }
    if (!WxPayConstant.RESULT_CODE_SUCCESS.equals(resultCode)
        || resultMap.get("err_code") != null) {
      String errCode = null;
      try {
        errCode = new String(resultMap.get("err_code").getBytes("iso-8859-1"), "utf-8");
        logger.info("err_code===" + errCode);
      } catch (Exception e) {
        throw new WxPayResultException("提交退款申请失败,原因：" + errCode);
      }
    } else {
      // 开始验签
      boolean verifySign = PayWxUtils.verfitySign(resultMap, wxPayData.getWxPaySignKey());
      if (!verifySign) {
        throw new SignErroException("得到微信服务器签名校验错误");
      }
      // 签名通过,更改订单状态
      Map<String, Object> extensions = wxOrderData.getExtensions();
      extensions.put("refundId", resultMap.get("refund_id"));
      wxOrderData.setExtensions(extensions);
      order.setOrderData(wxOrderData);
      order.setStatus(OrderConstant.ORDER_STATUS_REQUIRE_REFUND); // 更改为申请退款中的状态
      order.setUpdateTime(System.currentTimeMillis()); // 更改时间
      orderRepository.save(order); // 此时还未退款成功
    }
    return order;
  }

  @Override
  public String notifyRefund(String xml) {
    logger.info("==接收到微信退款回调==" + xml);
    // 退款回调的信息需要解密
    Map<String, String> map = XMLUtils.xmlStr2Map(xml); // 回调结果
    String returnCode = map.get("return_code"); // 微信支付服务器返回的状态码
    if (!WxPayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
      logger.info("==微信支付服务器回调退款请求retrun_code为fail==" + map.get("return_msg"));
      return PayWxUtils.returnFail();
    }
    String appId = map.get("appid"); // 公众账号id
    String wxMerchantId = map.get("mch_id"); // 微信支付分配的商户号
    Merchant merchant = merchantRepository.findByAppIdAndMchId(appId, wxMerchantId);
    if (merchant == null) {
      throw new PermissionFailException("找不到商户");
    }
    WxpayData wxPayData = merchant.getWxPayData(); // 对应的微信支付配置
    if (wxPayData == null) {
      throw new PermissionFailException("没有配置微信支付");
    }
    String encryptStr = map.get("req_info"); // 获得加密串
    String decryptStr = null;
    try {
      decryptStr = AESUtils.decryptData(encryptStr, wxPayData.getWxPaySignKey()); // 解密串
    } catch (Exception e) {
      logger.info("解密微信支付退款回调失败");
      return PayWxUtils.returnFail();
    }
    NotifyStreamInfoDto notifyStreamInfoDto = new NotifyStreamInfoDto(); // 丢进kafka的消息对象
    notifyStreamInfoDto.setNums(1); // 第一次发送
    notifyStreamInfoDto.setTradeType(OrderConstant.ORDER_WXTRADE);
    notifyStreamInfoDto.setNotifyType(OrderConstant.NOTIFY_REFUND); // 退款回调
    notifyStreamInfoDto.setTime(System.currentTimeMillis()); // 发送时间设为当前时间
    notifyStreamInfoDto.setMerchant(merchant);
    Map<String, String> returnMap = XMLUtils.xmlStr2Map(decryptStr);
    String merchantTradeNumber = returnMap.get("out_trade_no"); // 商户订单号
    String wxPayMerchantId = returnMap.get("mch_id"); // 商户号
    String transactionId = returnMap.get("transaction_id"); // 微信订单号
    Order order =
        orderRepository.findByMerchantTradeNumber(
            merchant.getMerchantId(), merchantTradeNumber); // 根据订单号获取对应的订单信息
    String merchantId = order.getMerchantId(); // 获得商户Id
    if (order.getStatus() == OrderConstant.ORDER_STATUS_REFUND) { // 如果退款成功
      return PayWxUtils.returnSuccess(); // 状态已经处理过的，直接返回成功
    }
    // 校验订单原总金额是否正确
    Integer totalAmount = (Integer.valueOf(returnMap.get("total_fee")));
    Integer originTotalAmount = order.getOrderData().getTotalAmount(); // 金额
    // 原订单金额不相等
    if (originTotalAmount != totalAmount) {
      return PayWxUtils.returnFail();
    }
    Integer refundAmount = (Integer.valueOf(returnMap.get("refund_fee"))); // 申请退款金额
    Integer settlementRefundAmount =
        (Integer.valueOf(returnMap.get("settlement_refund_fee"))); // 实际退款金额
    String refundRequestSource = returnMap.get("refund_request_source"); // 退款发起来源
    String refundAccount = returnMap.get("refund_account"); // 退款资金来源
    String refundStatus = returnMap.get("refund_status"); // 退款状态
    String refundRecvAccout = returnMap.get("refund_recv_accout"); // 退款入账账户
    logger.info("==退款回调时得到的退款状态为==" + refundStatus);
    if (!WxPayConstant.REFUND_STATUS_SUCCESS.equals(refundStatus)) {
      notifyStreamInfoDto.setResult(0);
      order.setStatus(OrderConstant.ORDER_STATUS_PAY); // 改为支付
      // 退款异常
      if (WxPayConstant.REFUND_STATUS_CHANGE.equals(refundStatus)) {
        notifyStreamInfoDto.setMesg("退款异常");
      } else notifyStreamInfoDto.setMesg("退款关闭");
    } else {
      // 校验成功，更改订单状态
      order.setStatus(OrderConstant.ORDER_STATUS_REFUND);
      order.setUpdateTime(System.currentTimeMillis());
      WxOrderData orderData = (WxOrderData) order.getOrderData();
      orderData.setRefundAmount(refundAmount);
      Map<String, Object> extensions = orderData.getExtensions();
      extensions.put("settlementRefundAmount", settlementRefundAmount);
      extensions.put("refundRequestSource", refundRequestSource);
      extensions.put("refundAccount", refundAccount);
      extensions.put("refundStatus", refundStatus);
      extensions.put("refundRecvAccout", refundRecvAccout);
      orderData.setExtensions(extensions);
      order.setOrderData(orderData);
      order.setUpdateTime(System.currentTimeMillis());
      orderRepository.save(order);
      notifyStreamInfoDto.setOrder(
          orderRepository.findByMerchantTradeNumber(merchantId, merchantTradeNumber));
      notifyStreamInfoDto.setResult(1);
      // 丢进kafka里
      Gson gson = new Gson();
      String json = gson.toJson(notifyStreamInfoDto);
      notifyProducter.sendNotifyMsg(json);
    }
    return PayWxUtils.returnSuccess();
  }
}
