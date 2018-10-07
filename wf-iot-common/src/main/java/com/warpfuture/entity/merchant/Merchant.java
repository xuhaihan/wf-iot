package com.warpfuture.entity.merchant;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

/** Created by fido on 2018/4/16. 商户 */
@ToString
@Data
@Document(collection = "iot_merchant_info")
public class Merchant {
  private String accountId; // 账户Id
  @Id private String merchantId; // 商户号Id
  private String merchantName; // 商户名称
  private AlipayData aliPayData; // 支付宝
  private WxpayData wxPayData; // 微信
  private String resultNotifyURL; // 支付结果回调地址
  private String merchantDesc; // 商户描述
  private Boolean isDelete; // 是否删除
  private Long createTime; // 创建时间
  private Long updateTime; // 更新时间
  private MultipartFile file;//微信退款证书

  public Merchant() {}

  public Merchant(String accountId, String merchantId) {
    this.accountId = accountId;
    this.merchantId = merchantId;
  }
}
