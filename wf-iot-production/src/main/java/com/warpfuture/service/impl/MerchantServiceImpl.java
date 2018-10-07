package com.warpfuture.service.impl;

import com.warpfuture.entity.PageModel;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.merchant.WxpayData;
import com.warpfuture.exception.ParameterIllegalException;
import com.warpfuture.exception.PermissionFailException;
import com.warpfuture.repository.MerchantRepository;
import com.warpfuture.repository.RefundBookRepository;
import com.warpfuture.service.MerchantService;
import com.warpfuture.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by fido on 2018/4/17. */
@Service
public class MerchantServiceImpl implements MerchantService {

  @Autowired private MerchantRepository merchantRepository;
  @Autowired private RefundBookRepository refundBookRepository;

  @Override
  public Merchant create(Merchant merchant) {
    WxpayData wxpayData = merchant.getWxPayData(); // 微信配置
    if (merchant.getWxPayData() != null) {
      String wxAppId = wxpayData.getWxPayAppId(); // appid
      String wxMerchantId = wxpayData.getWxPayMerchantId(); // mch_id
      Merchant findMerchant = merchantRepository.findByAppIdAndMchId(wxAppId, wxMerchantId);
      if (findMerchant != null) {
        throw new ParameterIllegalException("商户配置重复");
      }
    }
    String merchantId = IdUtils.getId(); // 随机生成商户id
    merchant.setCreateTime(System.currentTimeMillis());
    merchant.setUpdateTime(System.currentTimeMillis());
    merchant.setIsDelete(false); // 删除状态为false
    merchant.setMerchantId(merchantId);
    merchantRepository.create(merchant);
    return merchantRepository.findById(merchantId);
  }

  @Override
  public Merchant update(Merchant merchant) {
    Merchant originMerchant = merchantRepository.findById(merchant.getMerchantId());
    if (!originMerchant.getAccountId().equals(merchant.getAccountId())) {
      throw new PermissionFailException("更新商户权限不足");
    }
    originMerchant.setMerchantName(merchant.getMerchantName());
    originMerchant.setResultNotifyURL(merchant.getResultNotifyURL());
    originMerchant.setWxPayData(merchant.getWxPayData()); // 重新上传了退款证书
    String refundBookLocation = originMerchant.getWxPayData().getWxPayRefundBook(); // 退款oss
    if (refundBookLocation != null) {
      refundBookRepository.deleteById(refundBookLocation); // 把原来的退款证书删除了，后面支付的时候就会重新下载新的退款证书
    }
    originMerchant.setAliPayData(merchant.getAliPayData());
    originMerchant.setMerchantDesc(merchant.getMerchantDesc());
    originMerchant.setUpdateTime(System.currentTimeMillis());
    merchantRepository.update(originMerchant);
    return merchantRepository.findById(merchant.getMerchantId());
  }

  @Override
  public void delete(String accountId, String merchantId) {
    if (!ifHasPower(accountId, merchantId)) {
      throw new PermissionFailException("删除商户权限不足");
    }
    merchantRepository.delete(merchantId);
  }

  @Override
  public Merchant findById(String accountId, String merchantId) {
    if (!ifHasPower(accountId, merchantId)) {
      throw new PermissionFailException("查询商户权限不足");
    }
    return merchantRepository.findById(merchantId);
  }

  @Override
  public PageModel<Merchant> findByAccountId(
      String accountId, Integer pageIndex, Integer pageSize) {
    return merchantRepository.findByAccountId(accountId, pageIndex, pageSize);
  }

  /**
   * 是否有权限操作
   *
   * @param accountId
   * @param merchantId
   * @return
   */
  private boolean ifHasPower(String accountId, String merchantId) {
    Merchant originMerchant = merchantRepository.findById(merchantId);
    if (originMerchant == null) {
      return false;
    }
    if (!originMerchant.getAccountId().equals(accountId)) {
      return false;
    } else {
      return true;
    }
  }
}
