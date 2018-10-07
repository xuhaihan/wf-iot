package com.warpfuture.iot.protocol.service.impl.middleware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.warpfuture.dto.ProtocolRouteDto;
import com.warpfuture.iot.protocol.constant.Command;
import com.warpfuture.iot.protocol.constant.ResponseCode;
import com.warpfuture.entity.RepayEntity;
import com.warpfuture.iot.protocol.service.ConsumerService;
import com.warpfuture.iot.protocol.service.ReceiveChannel;
import com.warpfuture.iot.protocol.service.impl.WSServiceImpl;
import com.warpfuture.iot.protocol.service.impl.WTTServiceImpl;
import com.warpfuture.service.DeviceService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by 徐海瀚 on 2018/4/18. */
@EnableBinding(value = ReceiveChannel.class)
@Service
@Log4j2
public class ConsumerServiceImpl implements ConsumerService {

  @Autowired private DeviceService deviceService;

  @StreamListener(ReceiveChannel.INPUT1)
  public void receiveDataMsg(@Payload String payload) {
    try {
      JSONObject jsonObject = JSON.parseObject(payload);
      if (jsonObject != null) {
        if (jsonObject.containsKey("target")) {
          log.info("监听到下发的指令:{}", jsonObject);
          Map<String, Object> map = (Map<String, Object>) jsonObject.get("target");
          if (map != null) {
            String accountId = (String) jsonObject.get("accountId");
            String deviceId = (String) map.get("deviceId");
            String data = (String) jsonObject.get("data");
            Channel channel = getDeviceChannel(accountId, deviceId);
            if (channel != null && channel.isActive()) {
              ByteBuf dataByteBuf = Unpooled.buffer();
              dataByteBuf.writeBytes(data.getBytes());
              channel.writeAndFlush(dataByteBuf); // 发送数据
            }
          }
        } else if (jsonObject.containsKey("statement")) { // 监听到企业下发的指令
          if (jsonObject.get("statement") != null) {
            if (Command.ENTERPRISE_KILL_DIVICES.equals(jsonObject.get("statement"))) {
              Map<String, Object> map = (Map<String, Object>) jsonObject.get("data");
              String accountId = (String) map.get("accountId");
              List<String> list = (List<String>) map.get("objectList");
              if (accountId != null && list != null && !list.isEmpty()) {
                for (String deviceId : list) {
                  Channel channel = getDeviceChannel(accountId, deviceId);
                  if (channel != null) {
                    channel.close();
                  }
                }
              }
            } else if (Command.WF_SCREEN_CONNECTION.equals(jsonObject.get("statement"))) {
              WTTServiceImpl.setScreenValue(true);
            }
          }
        } else if (jsonObject.containsKey("source")) {
          log.info("监听到设备上报数据:{}", jsonObject);
          String accountId = (String) jsonObject.get("accountId");
          if (jsonObject.get("source") != null) {
            Map<String, Object> map = (Map<String, Object>) jsonObject.get("source");
            String productionId = (String) map.get("productionId");
            String deviceId = (String) map.get("deviceId");
            String data = (String) jsonObject.get("data");
            RepayEntity<List<String>> users = verifyDeviceToUser(productionId, deviceId);
            log.info("设备到用户鉴权返回用户列表:{}", users);
            if (users != null) {
              dispatchMsg(users, accountId, productionId, deviceId, data);
            }
          }
        }
      }
    } catch (Exception e) {
      log.info("监听消息时捕获到异常{}", e.getMessage());
      log.info("监听消息过程中捕获异常时的通信数据:{}", payload);
    }
  }

  @Override
  @StreamListener(ReceiveChannel.INPUT2)
  public synchronized void receiveLoginMsg(@Payload String payload) {

    log.info("登录连接相关记录:{}", payload);
  }

  @Override
  @StreamListener(ReceiveChannel.INPUT3)
  public synchronized void receiveOutlineMsg(@Payload String payload) {
    log.info("设备掉线相关记录:{}", payload);
  }

  /**
   * 根据下发的指令中包含的accountId，deviceId，选出对应的发送通道
   *
   * @param accountId 企业id
   * @param deviceId 设备id
   * @return
   */
  private Channel getDeviceChannel(String accountId, String deviceId) {
    Map<String, Map<String, Channel>> map = WTTServiceImpl.getChannelMap();
    if (accountId != null && deviceId != null) {
      if (map.containsKey(accountId)) {
        Map<String, Channel> map1 = map.get(accountId);
        if (map1.containsKey(deviceId)) {
          log.info("获取设备channel:{}", map1.get(deviceId));
          return map1.get(deviceId);
        }
      }
    }
    return null;
  }

  private List<Channel> getUserChannel(String accountId, String userId) {

    Map<String, Map<String, Object>> map = WSServiceImpl.getChannelMap();
    if (accountId != null && userId != null) {
      if (map.containsKey(accountId)) {
        Map<String, Object> map1 = map.get(accountId);
        List<Channel> channelList = (List<Channel>) map1.get(userId);
        return channelList;
      }
    }
    return null;
  }

  /**
   * 分发设备上报消息根据用户订阅的配置分发给用户
   *
   * @param accountId
   * @param productionId
   * @param deviceId
   * @param data
   */
  private void dispatchMsg(
      RepayEntity repayEntity,
      String accountId,
      String productionId,
      String deviceId,
      String data) {

    if (ResponseCode.ROUTE_DEVTOUSER_SUCCESS.getCode() == repayEntity.getCode()) {
      List<String> userList = (List<String>) repayEntity.getData();
      Map<String, Map<String, String>> map = WSServiceImpl.getUdMap();
      for (String user : userList) {
        log.info("user:{}", user);
        if (map.containsKey(user)) {
          Map<String, String> map1 = map.get(user);
          if (map1.get(productionId) != null) {
            if (deviceId.equals(map1.get(productionId))) {
              List<Channel> channelList = getUserChannel(accountId, user);
              for (Channel c : channelList) {
                if (c != null && c.isActive()) c.writeAndFlush(new TextWebSocketFrame(data));
              }
            }
          }
        }
      }
    }
  }

  /**
   * 设备到用户的鉴权
   *
   * @param productionId
   * @param deviceId
   * @return
   */
  private RepayEntity<List<String>> verifyDeviceToUser(String productionId, String deviceId) {
    ProtocolRouteDto protocolRouteDto = new ProtocolRouteDto();
    protocolRouteDto.setProductionId(productionId);
    protocolRouteDto.setDeviceId(deviceId);
    RepayEntity<List<String>> response = deviceService.devToUser(protocolRouteDto);
    if (response != null) return response;
    return null;
  }
}
