package com.warpfuture.entity.merchant;

import lombok.Data;
import lombok.ToString;

/** Created by fido on 2018/4/16. 支付宝支付相关配置 */
@Data
@ToString
public class AlipayData {
  private String aliPayAppId; // 支付宝分配给商户的应用Id
  private String aliPayMerchantPrivateKey; // 商家密钥
  private String aliPayPublicKey; // 支付宝公钥

  public AlipayData() {
  }

  public AlipayData(String aliPayAppId, String aliPayMerchantPrivateKey, String aliPayPublicKey) {
    this.aliPayAppId = aliPayAppId;
    this.aliPayMerchantPrivateKey = aliPayMerchantPrivateKey;
    this.aliPayPublicKey = aliPayPublicKey;
  }
}
