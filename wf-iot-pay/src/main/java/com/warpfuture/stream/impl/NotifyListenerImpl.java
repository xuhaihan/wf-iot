package com.warpfuture.stream.impl;

import com.google.gson.Gson;
import com.warpfuture.dto.Notify2MerchantDto;
import com.warpfuture.dto.NotifyStreamInfoDto;
import com.warpfuture.entity.NotifyRecord;
import com.warpfuture.entity.merchant.Merchant;
import com.warpfuture.enums.ResponseCode;
import com.warpfuture.repository.NotifyRecordRepository;
import com.warpfuture.stream.NotifyListener;
import com.warpfuture.stream.NotifyProducter;
import com.warpfuture.stream.ReceiveChannel;
import com.warpfuture.vo.ResultVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/** Created by fido on 2018/5/16. */
@Service
@EnableBinding(ReceiveChannel.class)
@Log4j2
public class NotifyListenerImpl implements NotifyListener {
  @Autowired private RestTemplate restTemplate;
  @Autowired private NotifyProducter notifyProducter;
  @Autowired private NotifyRecordRepository notifyRecordRepository;

  @StreamListener(ReceiveChannel.INPUT)
  public void receiveNotifyMsg(@Payload String payload) {
    log.info("==kafka开始消费数据===");
    Gson gson = new Gson();
    NotifyStreamInfoDto notifyStreamInfoDto = gson.fromJson(payload, NotifyStreamInfoDto.class);
    Merchant merchant = notifyStreamInfoDto.getMerchant();
    ResultVO<Notify2MerchantDto> resultVO = null;
    //  超过三次了，停止回调，需要记录进cassandra
    if (notifyStreamInfoDto.getNums() == 4) {
      NotifyRecord notifyRecord = new NotifyRecord();
      notifyRecord.setNums(notifyStreamInfoDto.getNums() - 1);
      notifyRecord.setRecordTime(new Date(System.currentTimeMillis()));
      notifyRecord.setOrderId(notifyStreamInfoDto.getOrder().getOrderId());
      notifyRecord.setType(notifyStreamInfoDto.getNotifyType());
      notifyRecord.setAccountId(notifyStreamInfoDto.getMerchant().getAccountId());
      notifyRecord.setMerchantId(notifyStreamInfoDto.getMerchant().getMerchantId());
      notifyRecordRepository.insert(notifyRecord);
    } else {
      if (notifyStreamInfoDto.getOrder() == null) {
        if (notifyStreamInfoDto.getMesg() != null)
          resultVO =
              new ResultVO<>(
                  ResponseCode.WXPAY_OPERATION_FAIL.getCode(), notifyStreamInfoDto.getMesg());
        else resultVO = new ResultVO<>(ResponseCode.WXPAY_OPERATION_FAIL.getCode(), "支付失败");
      } else {
        Notify2MerchantDto notify2MerchantDto = new Notify2MerchantDto();
        notify2MerchantDto.setTradeType(notifyStreamInfoDto.getTradeType());
        notify2MerchantDto.setNotifyType(notifyStreamInfoDto.getNotifyType());
        notify2MerchantDto.setOrder(notifyStreamInfoDto.getOrder());
        resultVO =
            new ResultVO<>(
                ResponseCode.WXPAY_OPERATION_SUCCESS.getCode(), "操作成功", notify2MerchantDto);
      }
      log.info("==发送给业务服务器的数据==" + gson.toJson(resultVO));
      String notifyURL = merchant.getResultNotifyURL(); // 返回支付结果给对方的业务服务器
      ResponseEntity<String> responseStr =
          restTemplate.postForEntity(notifyURL, resultVO, String.class);
      log.info("接收到业务服务器的回复==" + responseStr.getBody());
      //  成功，需要丢进cassandra
      if (responseStr.getBody().equals("SUCCESS")) {
        log.info("接收到业务服务器的成功回复");
        NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNums(notifyStreamInfoDto.getNums());
        notifyRecord.setRecordTime(new Date(System.currentTimeMillis()));
        notifyRecord.setOrderId(notifyStreamInfoDto.getOrder().getOrderId());
        notifyRecord.setType(notifyStreamInfoDto.getNotifyType());
        notifyRecord.setAccountId(notifyStreamInfoDto.getMerchant().getAccountId());
        notifyRecord.setMerchantId(notifyStreamInfoDto.getMerchant().getMerchantId());
        notifyRecordRepository.insert(notifyRecord);
      }
      // 失败
      else {
        notifyStreamInfoDto.setNums(notifyStreamInfoDto.getNums() + 1);
        notifyStreamInfoDto.setTime(System.currentTimeMillis());
        // 再次丢进kafka
        notifyProducter.sendNotifyMsg(gson.toJson(notifyStreamInfoDto));
      }
    }
  }
}
