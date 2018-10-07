package com.warpfuture.iot.api.console.dto;

import com.warpfuture.entity.merchant.AlipayData;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.merchant.WxpayData;
import com.warpfuture.util.CompareUtil;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MerchantFront {

    private String accountId; // 账户Id
    private String merchantId; // 商户号Id
    private String merchantName; // 商户名称
    private String resultNotifyURL; // 支付结果回调地址
    private String merchantDesc; // 商户描述
    private Boolean isDelete; // 是否删除

    private MultipartFile file;//微信退款证书

    private String wxPayAppId; // 微信支付分配的账号Id
    private String wxPayMerchantId; // 商户号
    private String wxPaySignKey; // 商户对应的密钥，用于sign签名
    private String wxPayRefundBook; // 商户微信支付对应的退款证书存放的地址

    private String aliPayAppId; // 支付宝分配给商户的应用Id
    private String aliPayMerchantPrivateKey; // 商家密钥
    private String aliPayPublicKey; // 支付宝公钥

    public Merchant toMerchant() {
        Merchant merchant = new Merchant();
        merchant.setAccountId(accountId);
        merchant.setFile(file);
        merchant.setMerchantId(merchantId);
        merchant.setIsDelete(isDelete);
        merchant.setMerchantName(merchantName);
        merchant.setResultNotifyURL(resultNotifyURL);
        merchant.setMerchantDesc(merchantDesc);

        if (CompareUtil.strNotNull(wxPayAppId) && CompareUtil.strNotNull(wxPayMerchantId) && CompareUtil.strNotNull(wxPaySignKey)) {
            merchant.setWxPayData(new WxpayData(wxPayAppId, wxPayMerchantId, wxPaySignKey, wxPayRefundBook));
        }
        if (CompareUtil.strNotNull(aliPayAppId) && CompareUtil.strNotNull(aliPayMerchantPrivateKey) && CompareUtil.strNotNull(aliPayPublicKey)) {
            merchant.setAliPayData(new AlipayData(aliPayAppId, aliPayMerchantPrivateKey, aliPayPublicKey));
        }
        return merchant;
    }
}
