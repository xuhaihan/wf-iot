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

/** @Auther: fido @Date: 2018/5/31 10:23 @Description:处理暂未收到回调的退款信息 */
@Component
@Slf4j
public class DealWxOverTimeRefund {
  @Autowired private OrderRepository orderRepository;
  @Autowired private RestTemplate restTemplate;
  @Autowired private WxPayService wxPayService;
  @Autowired private MerchantRepository merchantRepository;

  // 每隔30s查看尚未处理的订单
  @Scheduled(fixedDelay = 30 * 1000)
  public void finishOverTimeRefund() {
    log.info("==开始查找超时尚未得到回复的退款申请==");
    List<Order> notFinishRefund = orderRepository.getOverTimeWxRefund();
    for (Order order : notFinishRefund) {
      String merchantTradeNumber = order.getMerchantTradeNumber(); // 商户订单号
      String merchantId = order.getMerchantId(); // 商户号
      String queryParam = wxPayService.prepareQueryData(merchantId, merchantTradeNumber);
      // 去微信服务器主动查询订单状态
      ResponseEntity<String> responseEntity =
          restTemplate.postForEntity(WxPayConstant.QUERY_REFUND_URL, queryParam, String.class);
      log.info("==主动去微信支付服务器查询退款信息==" + responseEntity.getBody());
      Map<String, String> resultMap = XMLUtils.xmlStr2Map(responseEntity.getBody());
      // 得到微信服务器回复的订单信息，需要签名校验，更新订单状态
      String returnCode = resultMap.get("return_code"); // 通信标识
      String wxPayMerchantId = resultMap.get("mch_id"); // 微信支付的商户号
      String wxAppId = resultMap.get("appid");//appid
      log.info("==return_code为==" + returnCode);
      if (returnCode.equals(WxPayConstant.RETURN_CODE_SUCCESS)) {
        Merchant merchant = merchantRepository.findByAppIdAndMchId(wxAppId,wxPayMerchantId);
        WxpayData wxPayData = merchant.getWxPayData();
        WxOrderData orderData = (WxOrderData) order.getOrderData();
        Map<String, Object> extensions = orderData.getExtensions();
        boolean vertifySign = PayWxUtils.verfitySign(resultMap, wxPayData.getWxPaySignKey());
        // 签名通过
        if (vertifySign) {
          log.info("==校验主动查询时签名通过===");
          String resultCode = resultMap.get("result_code");
          if (!resultCode.equals(WxPayConstant.RESULT_CODE_SUCCESS)) {
            log.info("==主动查询退款状态，result_code为fail==" + resultMap.get("err_code"));
          } else {
            String refundCount = resultMap.get("refund_count"); // 退款笔数
            String refundId = resultMap.get("refundId"); // 微信退款单号
            String refundRecvAccout = resultMap.get("refund_recv_accout"); // 退款入账账户
            String refundStatus = resultMap.get("refund_status"); // 退款状态
            String refundErrorMesg = null;
            // 交易成功
            if (!WxPayConstant.REFUND_STATUS_SUCCESS.equals(refundStatus)) {
              order.setStatus(OrderConstant.ORDER_STATUS_PAY); // 改为支付
              // 退款异常
              if (WxPayConstant.REFUND_STATUS_CHANGE.equals(refundStatus)) {
                refundErrorMesg = "退款异常";
                extensions.put("refundErrorMesg", refundErrorMesg);

              } else {
                refundErrorMesg = "退款关闭";
                extensions.put("refundErrorMesg", refundErrorMesg);
              }
            }
            // 校验成功，更改订单状态
            else {
              order.setStatus(OrderConstant.ORDER_STATUS_REFUND);
              order.setUpdateTime(System.currentTimeMillis());
              extensions.put("refundId", refundId);
              extensions.put("refundCount", refundCount);
              extensions.put("refundStatus", refundStatus);
              extensions.put("refundRecvAccout", refundRecvAccout);
            }
            orderData.setExtensions(extensions);
            order.setOrderData(orderData);
            order.setUpdateTime(System.currentTimeMillis());
            orderRepository.save(order);
          }
        }
      }
    }
  }
}
