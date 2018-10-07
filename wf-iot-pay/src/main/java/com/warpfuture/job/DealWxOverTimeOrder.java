package com.warpfuture.job;

import com.warpfuture.constant.OrderConstant;
import com.warpfuture.constant.WxPayConstant;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.merchant.WxpayData;
import com.warpfuture.entity.order.Order;
import com.warpfuture.entity.order.WxOrderData;
import com.warpfuture.repository.MerchantRepository;
import com.warpfuture.repository.OrderRepository;
import com.warpfuture.service.WxPayService;
import com.warpfuture.util.DateUtils;
import com.warpfuture.util.PayWxUtils;
import com.warpfuture.util.XMLUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/** @Auther: fido @Date: 2018/5/22 09:29 @Description: */
@Component
@Slf4j
public class DealWxOverTimeOrder {
  @Autowired private OrderRepository orderRepository;
  @Autowired private RestTemplate restTemplate;
  @Autowired private WxPayService wxPayService;
  @Autowired private MerchantRepository merchantRepository;
  // 每隔30s查看尚未处理的订单
  @Scheduled(fixedDelay = 30 * 1000)
  public void finishOverTimeOrder() {
    log.info("==开始查找超时尚未得到回复的订单==");
    List<Order> notFinishOrder = orderRepository.getOverTimeWxQrder();
    for (Order order : notFinishOrder) {
      String merchantTradeNumber = order.getMerchantTradeNumber(); // 商户订单号
      String merchantId = order.getMerchantId(); // 商户号
      String queryParam = wxPayService.prepareQueryData(merchantId, merchantTradeNumber);
      // 去微信服务器主动查询订单状态
      ResponseEntity<String> responseEntity =
          restTemplate.postForEntity(WxPayConstant.quaryOrderURL, queryParam, String.class);
      log.info("==主动去微信支付服务器查询订单信息==" + responseEntity.getBody());
      Map<String, String> resultMap = XMLUtils.xmlStr2Map(responseEntity.getBody());
      // 得到微信服务器回复的订单信息，需要签名校验，更新订单状态
      String returnCode = resultMap.get("return_code"); // 通信标识
      String wxPayMerchantId = resultMap.get("mch_id"); // 微信支付的商户号
      String wxAppId = resultMap.get("appid"); // appid
      log.info("==return_code为==" + returnCode);
      if (returnCode.equals(WxPayConstant.RETURN_CODE_SUCCESS)) {
        Merchant merchant = merchantRepository.findByAppIdAndMchId(wxAppId, wxPayMerchantId);
        WxpayData wxPayData = merchant.getWxPayData();
        boolean vertifySign = PayWxUtils.verfitySign(resultMap, wxPayData.getWxPaySignKey());
        // 签名通过
        if (vertifySign) {
          log.info("==校验主动查询时签名通过===");
          String resultCode = resultMap.get("result_code");
          if (!resultCode.equals(WxPayConstant.RESULT_CODE_SUCCESS)) {
            // 当result_code为fail的情况
            log.info("==主动查询订单，result_code为fail==" + resultMap.get("err_code"));
          } else {
            String tradeResult = resultMap.get("trade_state");
            // 交易成功
            if (("SUCCESS").equals(tradeResult)) {
              log.info("==订单交易成功，开始处理订单状态==");
              order.setStatus(OrderConstant.ORDER_STATUS_PAY);
              WxOrderData wxOrderData = (WxOrderData) order.getOrderData();
              Map<String, Object> extensions = wxOrderData.getExtensions();
              String endTime = resultMap.get("time_end"); // 支付完成时间
              order.setUpdateTime(DateUtils.getTime(endTime)); // 更改更新时间
              wxOrderData.setWxTradeNumber("transaction_id");
              extensions.put("bankType", resultMap.get("bank_type"));
              extensions.put("cashAmount", resultMap.get("cash_fee"));
              wxOrderData.setWxOpenId(resultMap.get("openid"));
              // 应结订单金额
              if (resultMap.get("settlement_total_fee") != null) {
                wxOrderData.setRealAmount(Integer.valueOf(resultMap.get("settlement_total_fee")));
              }
              wxOrderData.setWxTradeNumber((String) resultMap.get("transaction_id"));
              wxOrderData.setExtensions(extensions);
              order.setOrderData(wxOrderData);
              orderRepository.save(order); // 保存
            }
            // trade_state为已关闭
            //            else if (("PAYERROR").equals(tradeResult)) {
            else {
              order.setStatus(OrderConstant.ORDER_STATUS_CANCEL);
              orderRepository.save(order);
            }
          }
        }
      }
    }
  }
}
