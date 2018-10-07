package com.warpfuture.util;

import com.warpfuture.constant.WxPayConstant;
import com.warpfuture.entity.merchant.WxpayData;

import java.util.HashMap;
import java.util.Map;

/** Created by fido on 2018/5/16. 处理使用微信JSAPI支付方式的工具类 */
public class WxpayJSAPIUtils {

  /**
   * 得到带有签名的数据包
   *
   * @param wxPayData
   * @param params
   * @return
   */
  public static String getSignPackage(WxpayData wxPayData, Map<String, String> params) {
    String sign = PayWxUtils.getMD5Sign(params, wxPayData.getWxPaySignKey());
    params.put("sign", sign);
    String signPackage = XMLUtils.map2str(params);
    return signPackage;
  }

  /**
   * 对于JSAPI而言，支付下单时需要返回给前端如下信息，封装好对应参数返回给前端
   *
   * @param resultMap
   * @param wxpayData
   * @return
   */
  public static Map<String, String> getReturn2JSAPI(
      Map<String, String> resultMap, WxpayData wxpayData) {
    String appId = wxpayData.getWxPayAppId();
    String wxPayMerchantId = wxpayData.getWxPayMerchantId();
    String tradeType = resultMap.get("trade_type");
    String perpayId = resultMap.get("prepay_id");
    String timeStamp = DateUtils.getTimeStamp(); // 时间戳
    Map<String, String> need2Sign = new HashMap<>();
    need2Sign.put("appId", appId);
    need2Sign.put("timeStamp", timeStamp);
    need2Sign.put("nonceStr", PayWxUtils.getNonceStr());
    need2Sign.put("signType", WxPayConstant.WX_SIGN_TYPE);
    need2Sign.put("package", "prepay_id=" + perpayId);
    String sign = PayWxUtils.getMD5Sign(need2Sign, wxpayData.getWxPaySignKey());
    need2Sign.put("paySign", sign);
    return need2Sign;
  }
}
