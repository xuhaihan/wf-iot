package com.warpfuture.util;

import com.warpfuture.dto.*;
import com.warpfuture.entity.*;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.entity.order.Order;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 判断工具,统一判断对象某些属性不为空
 */
public class CompareUtil {


    public static boolean deviceNotNull(Device device) {
        return device != null &&
                strNotNull(device.getDeviceId()) &&
                strNotNull(device.getProductionId());
    }

    public static boolean getJwtNotNull(UserEntity userEntity) {
        return userEntity != null &&
                strNotNull(userEntity.getAccountId()) &&
                strNotNull(userEntity.getApplicationId()) &&
                strNotNull(userEntity.getUserId());
    }

    public static boolean orderNotNull(Order order) {
        return order != null &&
                strNotNull(order.getMerchantTradeNumber()) &&
                strNotNull(order.getMerchantId());
    }

    public static boolean uploadDeviceInfoNotNull(UploadDeviceInfo uploadDeviceInfo) {
        return uploadDeviceInfo != null &&
                strNotNull(uploadDeviceInfo.getDeviceId()) &&
                strNotNull(uploadDeviceInfo.getOriginOtaVersion()) &&
                strNotNull(uploadDeviceInfo.getProductionKey());
    }

    /**
     * 如果Tag的 accountId、 tagId 都不为空则返回true
     *
     * @param tag
     * @return
     */
    public static boolean tagIdNotNull(Tag tag) {
        return tag != null &&
                strNotNull(tag.getTagId()) &&
                strNotNull(tag.getAccountId());
    }

    public static boolean tagNotNull(Tag tag) {
        return tag != null &&
                strNotNull(tag.getAccountId()) &&
                strNotNull(tag.getTagName()) &&
                strNotNull(tag.getTagDesc());
    }


    public static boolean userIdNotNull(UserEntity userEntity) {
        return userEntity != null &&
                strNotNull(userEntity.getAccountId()) &&
                strNotNull(userEntity.getUserId());
    }

    public static boolean userBasicNotNull(UserEntity userEntity) {
        return userEntity != null &&
                strNotNull(userEntity.getNickname()) &&
                strNotNull(userEntity.getPhone()) &&
                strNotNull(userEntity.getEmail());
    }

    public static boolean userExtensionNotNull(UserEntity userEntity) {
        return userEntity != null &&
                mapNotNull(userEntity.getExpand());
    }

    /**
     * accountId, applicationId 不为空
     *
     * @param applicationEntity
     * @return
     */
    public static boolean applicationIdNotNull(ApplicationEntity applicationEntity) {
        return applicationEntity != null &&
                strNotNull(applicationEntity.getAccountId()) &&
                strNotNull(applicationEntity.getApplicationId());
    }

    public static boolean applicationBasicNotNull(ApplicationEntity applicationEntity) {
        return applicationEntity != null &&
                strNotNull(applicationEntity.getApplicationName());
    }

    public static boolean applicationExpansionNotNull(ApplicationEntity applicationEntity) {
        return applicationEntity != null &&
                mapNotNull(applicationEntity.getExpand());
    }

    public static boolean applicationOAuthNotNull(ApplicationEntity applicationEntity) {
        return applicationEntity != null &&
                mapNotNull(applicationEntity.getOauthData()) &&
                listNotNull(applicationEntity.getOauthType());
    }

    /**
     * 判断固件信息不为空
     *
     * @param otaInfo
     * @return
     */
    public static boolean otaNotNull(OTAInfo otaInfo) {
        return otaInfo != null &&
                strNotNull(otaInfo.getAccountId()) &&
                strNotNull(otaInfo.getOtaName()) &&
                strNotNull(otaInfo.getProductionId()) &&
                otaInfo.getOtaRole() != null &&
                strNotNull(otaInfo.getOtaVersion()) &&
                otaInfo.getOtaStatus() != null &&
                otaInfo.getFile() != null &&
                otaInfo.getFile().getSize() < 1024 * 1024 * 10;// 10MB
    }

    /**
     * 产品信息不为空
     *
     * @param production
     * @return
     */
    public static boolean productionNotNull(Production production) {
        return production != null &&
                strNotNull(production.getAccountId()) &&
                strNotNull(production.getProductionName()) &&
                strNotNull(production.getProductionLogo()) &&
                production.getEncryptRole() != null &&
                production.getEncryptRole().length > 0;
    }

    public static boolean productionIdNotNull(Production production) {
        return production != null &&
                strNotNull(production.getProductionId()) &&
                strNotNull(production.getAccountId());
    }

    public static boolean productionBasicNotNull(Production production) {
        return production != null &&
                strNotNull(production.getProductionName()) &&
                strNotNull(production.getProductionDesc()) &&
                production.getEncryptRole() != null &&
                production.getEncryptRole().length > 0 &&
                strNotNull(production.getProductionLogo());
    }

    public static boolean merchantCreateNotNull(Merchant merchant) {
        return merchant != null &&
                strNotNull(merchant.getMerchantName()) &&
                ((merchant.getWxPayData() != null &&//如果微信支付不为空，则id，密钥，签名都不为空，反之不校验
                        strNotNull(merchant.getResultNotifyURL()) &&
                        strNotNull(merchant.getWxPayData().getWxPayAppId()) &&
                        strNotNull(merchant.getWxPayData().getWxPayMerchantId()) &&
                        strNotNull(merchant.getWxPayData().getWxPaySignKey())) ||
                        merchant.getWxPayData() == null) &&
                ((merchant.getAliPayData() != null &&//如果支付宝支付不为空，则id，密钥，签名都不为空，反之不校验
                        strNotNull(merchant.getResultNotifyURL()) &&
                        strNotNull(merchant.getAliPayData().getAliPayAppId()) &&
                        strNotNull(merchant.getAliPayData().getAliPayMerchantPrivateKey()) &&
                        strNotNull(merchant.getAliPayData().getAliPayPublicKey())) ||
                        merchant.getAliPayData() == null);
    }

    public static boolean merchantIdNotNull(Merchant merchant) {
        return merchant != null &&
                strNotNull(merchant.getAccountId()) &&
                strNotNull(merchant.getMerchantId());
    }

    public static boolean otaIdNotNull(OTAInfo otaInfo) {
        return otaInfo != null &&
                strNotNull(otaInfo.getAccountId()) &&
                strNotNull(otaInfo.getProductionId());
    }

    public static boolean connectAutoDtoNotNull(ConnectAutoDto connectAutoDto) {
        return connectAutoDto != null &&
                strNotNull(connectAutoDto.getAccountId()) &&
                strNotNull(connectAutoDto.getApplicationId()) &&
                strNotNull(connectAutoDto.getProductionId()) &&
                strNotNull(connectAutoDto.getDeviceId()) &&
                strNotNull(connectAutoDto.getUserId());
    }

    public static boolean routeFrontNotNull(RouteFront routeFront) {
        return routeFront != null &&
                strNotNull(routeFront.getAccountId()) &&
                strNotNull(routeFront.getApplicationId());
    }

    public static boolean communicateMsgNotNull(CloudCommunicateMsg cloudCommunicateMsg) {
        return cloudCommunicateMsg != null &&
                cloudCommunicateMsg.getTarget() != null &&
                strNotNull(cloudCommunicateMsg.getTarget().getProductionId()) &&
                strNotNull(cloudCommunicateMsg.getTarget().getDeviceId());
    }

    /**
     * 去除前后空格后不为空
     *
     * @param str
     * @return
     */
    public static boolean strNotNull(String str) {
        return !StringUtils.isEmpty(StringUtils.trimWhitespace(str));
    }

    private static boolean mapNotNull(Map map) {
        return map != null && !map.isEmpty();
    }

    private static boolean listNotNull(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean historyDataIdNotNull(HistoryDataInfo dataInfo) {
        return dataInfo != null &&
                strNotNull(dataInfo.getDeviceId()) &&
                strNotNull(dataInfo.getProductionId());
    }

    public static boolean historyDataTypeNotNull(HistoryDataInfo dataInfo) {
        return dataInfo != null &&
                strNotNull(dataInfo.getDeviceId()) &&
                strNotNull(dataInfo.getProductionId()) &&
                dataInfo.getDataType() != null;
    }

    public static boolean historyDataTimeNotNull(HistoryDataInfo dataInfo) {
        return dataInfo != null &&
                strNotNull(dataInfo.getDeviceId()) &&
                strNotNull(dataInfo.getProductionId()) &&
                dataInfo.getStartTime() != null &&
                dataInfo.getEndTime() != null;
    }

    public static boolean createOrderNotNull(PayOrder payOrder) {
        return payOrder != null &&
                strNotNull(payOrder.getMerchantId()) &&
                payOrder.getParams() != null &&
                strNotNull(payOrder.getParams().getBody()) &&
                strNotNull(payOrder.getParams().getOut_trade_no()) &&
                strNotNull(payOrder.getParams().getTotal_fee()) &&
                strNotNull(payOrder.getParams().getTrade_type()) &&
                strNotNull(payOrder.getParams().getToken());
    }

    public static boolean createUserNotNull(UserEntity userEntity) {
        return userEntity != null &&
                strNotNull(userEntity.getNickname()) &&
                strNotNull(userEntity.getPhone()) &&
                strNotNull(userEntity.getEmail())&&
                strNotNull(userEntity.getPassword())&&
                strNotNull(userEntity.getApplicationId());
    }
}
