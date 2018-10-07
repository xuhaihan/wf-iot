package com.warpfuture.entity.merchant;
//

import lombok.Data;
import lombok.ToString;

/** Created by fido on 2018/4/16. 微信支付相关配置 */
@Data
@ToString
public class WxpayData {
  private String wxPayAppId; // 微信支付分配的账号Id
  private String wxPayMerchantId; // 商户号
  private String wxPaySignKey; // 商户对应的密钥，用于sign签名
  private String wxPayRefundBook; // 商户微信支付对应的退款证书存放的地址

  public WxpayData() {
  }

  public WxpayData(String wxPayAppId, String wxPayMerchantId, String wxPaySignKey, String wxPayRefundBook) {
    this.wxPayAppId = wxPayAppId;
    this.wxPayMerchantId = wxPayMerchantId;
    this.wxPaySignKey = wxPaySignKey;
    this.wxPayRefundBook = wxPayRefundBook;
  }
}
