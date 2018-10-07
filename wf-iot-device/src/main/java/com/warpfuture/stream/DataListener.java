package com.warpfuture.stream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.warpfuture.constant.AliveConstant;
import com.warpfuture.entity.Device;
import com.warpfuture.entity.HistoricalData;
import com.warpfuture.entity.NotifyMsg;
import com.warpfuture.entity.ReportEntity;
import com.warpfuture.repository.DeviceRepository;
import com.warpfuture.repository.NotifyMsgRepository;
import com.warpfuture.service.HistoryDataService;
import com.warpfuture.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Created by fido on 2018/4/19. 监听设备上报数据，更新设备在线状态 */
@EnableBinding(Sink.class)
public class DataListener {

  @Autowired private RedisService redisService;
  @Autowired private DeviceRepository deviceRepository;
  @Autowired private HistoryDataService historyDataService;
  @Autowired private RestTemplate restTemplate;
  @Autowired private NotifyMsgRepository notifyMsgRepository;

  private Logger logger = LoggerFactory.getLogger(DataListener.class);

  private static ConcurrentHashMap<String, Long> mesgRecord = new ConcurrentHashMap<>(); // 用于缓存
  private static ConcurrentHashMap<String, String> ipRecord = new ConcurrentHashMap<>(); // 用于记录ip

  @StreamListener(Sink.INPUT)
  public void handleMessage(@Payload String payload) {
    logger.info("--接收到的kafka信息为：--" + payload);
    // 得到上报的数据
    try {
      logger.info("==当前的map==" + mesgRecord);
      JSONObject jsonObject = JSON.parseObject(payload);
      if (jsonObject != null) {
        // 属于用户控制指令
        if (jsonObject.containsKey("target")) {
          Map<String, Object> map = (Map<String, Object>) jsonObject.get("target");
          String accountId = (String) jsonObject.get("accountId");
          String productionId = (String) map.get("productionId");
          String deviceId = (String) map.get("deviceId");
          Long messageTime = (Long) jsonObject.get("messageTime");
          String data = (String) jsonObject.get("data");
          HistoricalData historicalData = new HistoricalData();
          String historicalDataId =
              accountId
                  + ":"
                  + productionId
                  + ":"
                  + deviceId; // 通信历史数据以accountId:productionId:deviceId为主键
          historicalData.setHistoryDataId(historicalDataId);
          historicalData.setDataType(0); // 属于下发数据
          historicalData.setDataContent(data);
          historicalData.setDataTime(new Date(messageTime));
          historyDataService.insert(historicalData);
        } else if (jsonObject.containsKey("source")) {
          ReportEntity reportEntity =
              JSON.parseObject(payload, new TypeReference<ReportEntity>() {});
          Map<String, Object> map = (Map<String, Object>) reportEntity.getSource();
          String deviceId = (String) map.get("deviceId"); // 设备Id
          String deviceIp = (String) map.get("deviceIp"); // 设备Ip
          String sessionId = (String) map.get("sessionId"); // 会话Id
          Long time = reportEntity.getMessageTime(); // 本次通信数据的时间
          String dataContent = reportEntity.getData(); // 通信内容
          String productionId = (String) map.get("productionId"); // 产品Id
          String accountId = reportEntity.getAccountId(); // 账户Id
          if (deviceId != null
              && deviceIp != null
              && sessionId != null
              && productionId != null
              && accountId != null) {
            // 将本次历史通信数据插入进cassandra
            HistoricalData historicalData = new HistoricalData();
            String historicalDataId =
                accountId
                    + ":"
                    + productionId
                    + ":"
                    + deviceId; // 通信历史数据以accountId:productionId:deviceId为主键
            historicalData.setHistoryDataId(historicalDataId);
            historicalData.setDeviceIp(deviceIp);
            historicalData.setDataType(1); // 属于上报数据
            historicalData.setDataContent(dataContent);
            historicalData.setDataTime(new Date(time));
            historyDataService.insert(historicalData);
            // 维持设备在线状态
            String device = accountId + ":" + productionId + ":" + deviceId;
            mesgRecord.put(device, time);
            ipRecord.put(device, deviceIp);
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(
                new Runnable() {
                  @Override
                  public void run() {
                    for (Map.Entry<String, Long> entry : mesgRecord.entrySet()) {
                      String device = entry.getKey();
                      long mesgTime = entry.getValue();
                      if (mesgTime > 0L) {
                        logger.info("==开始处理更新==");
                        String[] args = device.split(":");
                        String accountId = args[0];
                        String productionId = args[1];
                        StringBuffer deviceId = new StringBuffer();
                        if (args.length > 3) {
                          for (int i = 2; i < args.length; i++) {
                            deviceId.append(args[i]);
                          }
                        } else {
                          deviceId.append(args[2]);
                        }
                        Device originDevice =
                            deviceRepository.findById(accountId, productionId, deviceId.toString());
                        logger.info("==更新设备在线状态==" + originDevice);
                        originDevice.setSendMsgTime(mesgTime);
                        originDevice.setDeviceIp(ipRecord.get(device));
                        deviceRepository.updateDevice(originDevice);
                        mesgRecord.remove(device);
                        ipRecord.remove(device);
                      }
                    }
                  }
                },
                0,
                AliveConstant.SCAN_TIME,
                TimeUnit.MILLISECONDS);
            // 发送给业务方
            Optional<NotifyMsg> optionalNotifyMsg = notifyMsgRepository.findById(accountId);
            if (optionalNotifyMsg.isPresent()) {
              NotifyMsg notifyMsg = optionalNotifyMsg.get();
              if (notifyMsg != null) {
                if (notifyMsg.getMsgNotifyURL() != null && notifyMsg.getIsAccept()) {
                  String notifyMsgURL = notifyMsg.getMsgNotifyURL();
                  restTemplate.postForEntity(notifyMsgURL, reportEntity, String.class);
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("kafka消费数据格式转换出错");
    }
  }
}
