package com.warpfuture.iot.protocol.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.warpfuture.dto.DeviceLoginDto;
import com.warpfuture.entity.RepayEntity;
import com.warpfuture.iot.protocol.constant.AuthMode;
import com.warpfuture.iot.protocol.constant.ResponseCode;
import com.warpfuture.iot.protocol.entity.RecordEntity;
import com.warpfuture.iot.protocol.entity.ReportEntity;
import com.warpfuture.iot.protocol.exception.DeviceAuthException;
import com.warpfuture.iot.protocol.service.ProducerService;
import com.warpfuture.iot.protocol.service.impl.WTTServiceImpl;
import com.warpfuture.service.DeviceService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Created by 徐海瀚 on 2018/4/12. WTT协议 处理器 */
@ChannelHandler.Sharable
@Log4j2
public class WTTHandler extends SimpleChannelInboundHandler<ByteBuf> {

  private ProducerService producerService; // 消费者服务

  private DeviceService deviceService; // 设备鉴权服务

  private RepayEntity<Map<String, Object>> response;

  public WTTHandler(ProducerService producerService, DeviceService deviceService) {
    this.producerService = producerService;
    this.deviceService = deviceService;
  }

  @Override
  public void channelActive(ChannelHandlerContext context) {
    if (WTTServiceImpl.getScreen()) { // 判断是否屏蔽新连接
      context.close();
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext context, ByteBuf msg) throws Exception {
    Channel channel = context.channel();
    byte[] byteArray = new byte[msg.capacity()];
    msg.readBytes(byteArray);
    String data = new String(byteArray);
    if (WTTServiceImpl.defaultChannelGroup.find(channel.id()) == null) {
      log.info("收到设备{}登录认证数据:{}", context.channel().id(), data);
      Map<String, Object> map = parseMessage(data);
      log.info("解析设备{}登录认证数据:{}", context.channel().id(), map.toString());
      if (!map.isEmpty()) {
        RepayEntity<Map<String, Object>> repay = loginAuth(map);
        if (repay != null) {
          response = repay;
          String formatData = toRecordMsg(context, response);
          if (formatData != null) {
            log.info("设备登录日志记录:{}", formatData);
            producerService.sendLoginMsg(formatData); // 登陆失败与否都记录在消息队列
          }
          if (ResponseCode.LOGIN_AUTH_SUCCESS.getCode() == response.getCode()) {
            // 保存channel,根据是否有连接成功的channel鉴别是否重复登陆
            log.info("设备连接通道{}登陆成功时间:{}", context.channel().id(), System.currentTimeMillis());
            WTTServiceImpl.defaultChannelGroup.add(channel);
            storeChannel(context, response);
          } else {
            context.close(); // 校验不成功断开连接
          }
        }
      } else {
        context.close();
      }
    } else {
      String formatData = toReportMsg(context, response, data);
      if (formatData != null) producerService.sendDataMsg(formatData); // 将设备上报数据放到消息队列
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext context) {
    // 设备不正常断线
    WTTServiceImpl.defaultChannelGroup.remove(context.channel());
    removeChannel(response);
    String outlineData = toRecordMsg(context, response);
    if (outlineData != null) {
      producerService.sendOutlineMsg(outlineData);
    }
    context.close();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext context, Throwable couse) {

    WTTServiceImpl.defaultChannelGroup.remove(context.channel());
    context.close();
    log.info("客户端{}发生{}异常", context.channel().id(), couse.getLocalizedMessage());
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    if (evt instanceof IdleStateEvent) {
      IdleStateEvent e = (IdleStateEvent) evt;
      if (e.state() == IdleState.READER_IDLE) {
        WTTServiceImpl.defaultChannelGroup.remove(ctx.channel());
        removeChannel(response);
        ctx.close();
        log.info("发现设备{}超过30秒未发消息，服务端READER_IDLE读超时", ctx.channel().id());
      } else {
        super.userEventTriggered(ctx, evt);
      }
    }
  }

  /**
   * 解析设备登陆认证数据
   *
   * @param msg 设备登陆认证直接拿到的透传数据
   * @return
   */
  private Map parseMessage(String msg) {
    String[] array = msg.split("\\s+");
    Map<String, Object> data = new LinkedHashMap<>();
    if (array != null && (array.length == 3 || array.length == 4)) {
      AuthMode authMode = AuthMode.getByValue(array[1]);
      if ("@#*WF".equals(array[0])) {
        if (authMode.getDesc() != null) {
          switch (authMode) {
            case N:
              data.put("productionKey", array[2]);
              data.put("deviceId", array[3]);
              break;
            case ECC:
              data.put("mode", "ECC");
              data.put("pksToken", array[2]);
              break;
            case RSA:
              data.put("mode", "RSA");
              data.put("pksToken", array[2]);
              break;
            default:
              break;
          }
        }
      }
    }
    return data;
  }

  /**
   * 设备登陆认证
   *
   * @param authData 设备登陆认证的解析后的数据
   * @return
   */
  public RepayEntity<Map<String, Object>> loginAuth(Map<String, Object> authData) {
    RepayEntity<Map<String, Object>> response = null;
    try {
      String mode = (String) authData.get("mode");
      DeviceLoginDto deviceLoginDto = new DeviceLoginDto();
      if (mode != null) {
        if (mode.equals("ECC") || mode.equals("RSA")) { // 强校验
          deviceLoginDto.setPksToken((String) authData.get("pksToken"));
          deviceLoginDto.setMode(mode);
          response = deviceService.checkWithSecure(deviceLoginDto);
          log.info("设备强校验的结果:{}", response);
          return response;
        }
      } else { // 弱校验
        deviceLoginDto.setDeviceId((String) authData.get("deviceId"));
        deviceLoginDto.setProductionKey((String) authData.get("productionKey"));
        response = deviceService.checkWithKey(deviceLoginDto);
        log.info("设备弱校验的结果:{}", response);
        return response;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new DeviceAuthException("Device LoginAuth Failure");
    }
    return response;
  }

  /**
   * 将调用接口应答消息整合转化成日志记录
   *
   * @param context
   * @param repayEntity 调用接口响应数据对应后端的实体类
   * @return
   */
  private String toRecordMsg(
      ChannelHandlerContext context, RepayEntity<Map<String, Object>> repayEntity) {
    RecordEntity<Map<String, Object>> recordEntity = new RecordEntity<>();
    if (repayEntity != null) {
      try {
        recordEntity.setMessage(repayEntity.getMessage());
        recordEntity.setMessageTime(System.currentTimeMillis());
        Map<String, Object> map = repayEntity.getData();
        if (map != null && !map.isEmpty()) {
          InetSocketAddress inetSocketAddress =
              (InetSocketAddress) context.channel().remoteAddress();
          String deviceIp = inetSocketAddress.getAddress().getHostAddress();
          map.put("deviceIp", deviceIp);
          recordEntity.setData(map);
        } else {
          recordEntity.setData(null);
        }
        return JSON.toJSONString(recordEntity);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * 将调用接口应答消息整合转化成设备上报消息
   *
   * @param context
   * @param repayEntity 调用接口响应数据对应后端的实体类
   * @param data 设备透传的数据
   * @return
   */
  private String toReportMsg(
      ChannelHandlerContext context, RepayEntity<Map<String, Object>> repayEntity, String data) {
    ReportEntity reportEntity = new ReportEntity();
    try {
      reportEntity.setAccountId((String) repayEntity.getData().get("accountId"));
      Map<String, Object> map = new HashMap<>();
      map.put("productionId", repayEntity.getData().get("productionId"));
      map.put("deviceId", repayEntity.getData().get("deviceId"));
      InetSocketAddress inetSocketAddress = (InetSocketAddress) context.channel().remoteAddress();
      String deviceIp = inetSocketAddress.getAddress().getHostAddress();
      map.put("deviceIp", deviceIp);
      map.put("sessionId", context.channel().id().asShortText());
      reportEntity.setSource(map);
      reportEntity.setMessageTime(System.currentTimeMillis());
      reportEntity.setData(data);
      return JSON.toJSONString(reportEntity);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将channel保存到一个map，具有<企业id，<设备id，channel>>的结构
   *
   * @param context
   * @param response 调用接口响应数据对应后端的实体类
   */
  private void storeChannel(
      ChannelHandlerContext context, RepayEntity<Map<String, Object>> response) {
    Map<String, Map<String, Channel>> channelMap = WTTServiceImpl.getChannelMap();
    String accountId = (String) response.getData().get("accountId");
    String deviceId = (String) response.getData().get("deviceId");
    if (accountId != null && deviceId != null) {
      Channel channel = context.channel();
      if (channelMap.containsKey(accountId)) {
        Map<String, Channel> map = WTTServiceImpl.getChannelMap().get(accountId);
        if (map == null) {
          map = new ConcurrentHashMap<>();
        }
        map.put(deviceId, channel);
      } else {
        Map<String, Channel> map = new ConcurrentHashMap<>();
        map.put(deviceId, channel);
        channelMap.put(accountId, map);
      }
    }
  }

  /**
   * 断线删除channel，更新netty服务端维护的wttMap
   *
   * @param response 调用接口响应数据对应后端的实体类
   */
  private void removeChannel(RepayEntity<Map<String, Object>> response) {
    Map<String, Map<String, Channel>> channelMap = WTTServiceImpl.getChannelMap();
    if (response != null) {
      if (response.getData() != null) {
        String accountId = (String) response.getData().get("accountId");
        String deviceId = (String) response.getData().get("deviceId");
        if (accountId != null && deviceId != null) {
          if (channelMap.containsKey(accountId)) {
            Map<String, Channel> map = WTTServiceImpl.getChannelMap().get(accountId);
            map.remove(deviceId);
          }
        }
      }
    }
  }
}
