package com.warpfuture.iot.protocol.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.warpfuture.dto.ProtocolRouteDto;
import com.warpfuture.iot.protocol.constant.ResponseCode;
import com.warpfuture.iot.protocol.entity.IssuedEntity;
import com.warpfuture.iot.protocol.entity.RecordEntity;
import com.warpfuture.entity.RepayEntity;
import com.warpfuture.iot.protocol.exception.TokenAuthFailException;
import com.warpfuture.iot.protocol.exception.UserAuthException;
import com.warpfuture.iot.protocol.service.ProducerService;
import com.warpfuture.iot.protocol.service.impl.WSServiceImpl;
import com.warpfuture.service.DeviceService;
import com.warpfuture.service.OauthService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;

/** Created by 徐海瀚 on 2018/4/15. WebSocket 处理器 */
@ChannelHandler.Sharable
@Log4j2
public class WebSocketHandler extends SimpleChannelInboundHandler {

  private WebSocketServerHandshaker handshaker;

  private DeviceService deviceService;

  private OauthService oauthService;

  private ProducerService producerService;

  private IssuedEntity issuedEntity = new IssuedEntity();

  public WebSocketHandler(
      DeviceService deviceService, OauthService oauthService, ProducerService producerService) {
    this.deviceService = deviceService;
    this.producerService = producerService;
    this.oauthService = oauthService;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    // http
    if (msg instanceof FullHttpRequest) {
      handleHttpRequest(ctx, (FullHttpRequest) msg);
    } else if (msg instanceof WebSocketFrame) {
      // WebSocket
      handleWebSocketFrame(ctx, (WebSocketFrame) msg);
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) {}

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void channelInactive(ChannelHandlerContext context) {
    // 用户不正常断线
    removeUd(issuedEntity);
    removeChannel(context, issuedEntity);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    if (!(cause instanceof IOException)) {
      cause.printStackTrace();
    }
    ctx.close();
  }

  /**
   * 握手后消息处理
   *
   * @param ctx
   * @param msg
   */
  private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {

    // 关闭链路指令
    if (msg instanceof CloseWebSocketFrame) {
      handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
      return;
    }

    // PING 消息
    if (msg instanceof PingWebSocketFrame) {
      ctx.write(new PongWebSocketFrame(msg.content().retain()));
      return;
    }

    // 非文本
    if (!(msg instanceof TextWebSocketFrame)) {
      throw new UnsupportedOperationException(
          String.format("%s frame type not support", msg.getClass().getName()));
    }

    // 长连接上来逻辑处理过程
    String request = ((TextWebSocketFrame) msg).text();
    try {
      Map<String, Map<String, String>> udMap = WSServiceImpl.getUdMap();
      String userId = null;
      if (issuedEntity.getSource() != null) {
        userId = (String) issuedEntity.getSource().get("userId");
      }
      if (userId != null && udMap.containsKey(userId)) {
        issuedEntity.setData(request);
        Map<String, Object> target = new HashMap<>();
        Map<String, String> ud = udMap.get(userId);
        for (String key : ud.keySet()) {
          target.put("productionId", key);
          target.put("deviceId", ud.get(key));
        }
        issuedEntity.setTarget(target);
      } else {
        JSONObject jsonObject = JSON.parseObject(request);
        if (jsonObject != null) {
          issuedEntity.setTarget((Map<String, Object>) jsonObject.get("target"));
          Map<String, Object> map = (Map<String, Object>) jsonObject.get("source");
          issuedEntity.setData((String) jsonObject.get("data"));
          String deviceId = (String) map.get("deviceId"); // 用户设备的id
          issuedEntity.getSource().put("deviceId", deviceId);
        }
      }
      RepayEntity<Boolean> repay = verifyUserToDev(issuedEntity);
      log.info("用户鉴权结果:{}", repay);
      if (repay != null) {
        if (ResponseCode.ROUTE_USERTODEV_SUCCESS.getCode() == repay.getCode()) {
          String data = JSONObject.toJSONString(issuedEntity);
          producerService.sendDataMsg(data); // 发送用户下发指令到消息队列
        } else {
          if (WSServiceImpl.getIsKillUser()) ctx.close();
        }
        RecordEntity<Map<String, Object>> recordEntity = new RecordEntity<>();
        recordEntity.setMessage(repay.getMessage());
        recordEntity.setMessageTime(System.currentTimeMillis());
        recordEntity.setData(issuedEntity.getSource());
        String data = JSONObject.toJSONString(recordEntity);
        producerService.sendLoginMsg(data); // 将鉴权与否的失败的记录送到响应的消息队列
      }
    } catch (UserAuthException e) {
      log.error("用户鉴权过程失败:{}", e.getMessage());
    }
  }

  /**
   * 握手前http请求处理
   *
   * @param ctx
   * @param msg
   */
  private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {

    // HTTP 请异常
    if (!msg.decoderResult().isSuccess() || !"websocket".equals(msg.headers().get("Upgrade"))) {
      log.info(msg.decoderResult().isSuccess());
      log.info(msg.headers().get("Upgrade"));
      sendHttpResponse(
          ctx,
          msg,
          new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
      return;
    }

    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(msg.uri());
    Map<String, List<String>> params = queryStringDecoder.parameters();
    String token = null;
    String productionId = null;
    String deviceId = null;
    if (params.get("token") != null) {
      token = params.get("token").get(0);
    }
    if (params.get("productionId") != null) {
      productionId = params.get("productionId").get(0);
    }
    if (params.get("deviceId") != null) {
      deviceId = params.get("deviceId").get(0);
    }
    if (token != null) {
      try {
        RepayEntity<Map<String, Object>> repayEntity = verifyToken(token);
        if (repayEntity != null) {
          RecordEntity<Map<String, Object>> recordEntity = new RecordEntity<>();
          recordEntity.setMessage(repayEntity.getMessage());
          if (ResponseCode.USER_TOKEN_FAIL.getCode() == repayEntity.getCode()) {
            Map<String, Object> map = new HashMap<>();
            InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String userIp = inetSocketAddress.getAddress().getHostAddress();
            map.put("userIp", userIp);
            map.put("token", token);
            recordEntity.setData(map);
            if (!WSServiceImpl.getUserResponse()) ctx.writeAndFlush("user token verify fail!\n");
            sendHttpResponse(
                ctx,
                msg,
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
          } else {
            recordEntity.setData(repayEntity.getData()); // 记录token校验成功的数据
            issuedEntity.setMessageTime(System.currentTimeMillis());
            issuedEntity.setAccountId((String) repayEntity.getData().get("accountId"));
            repayEntity.getData().remove("accountId");
            issuedEntity.setSource(repayEntity.getData());
            issuedEntity.getSource().put("sessionId", ctx.channel().id().asShortText());
            String userId = (String) issuedEntity.getSource().get("userId");
            if (productionId != null && deviceId != null)
              storeUd(userId, productionId, deviceId); // 配置用户订阅某台设备
            shakeHands(ctx, msg); // token校验成功就去握手
            log.info("用户{}连接成功时间:{}", ctx.channel().id(), System.currentTimeMillis());
          }
          String record = JSONObject.toJSONString(recordEntity);
          producerService.sendLoginMsg(record);
        }
      } catch (TokenAuthFailException e) {
        log.error("用户token验证过程捕获异常{}", e.getMessage());
      }
    }
  }

  /**
   * 响应用户请求
   *
   * @param ctx
   * @param msg
   * @param resp
   */
  private void sendHttpResponse(
      ChannelHandlerContext ctx, FullHttpRequest msg, FullHttpResponse resp) {

    // 响应
    if (resp.status().code() != 200) {
      ByteBuf buf = Unpooled.copiedBuffer(resp.status().toString(), CharsetUtil.UTF_8);
      resp.content().writeBytes(buf);
      buf.release();
      setContentLength(resp, resp.content().readableBytes());
    }

    // 非Keep-Alive,关闭链接
    ChannelFuture future = ctx.channel().writeAndFlush(resp);
    if (!isKeepAlive(resp) || resp.status().code() != 200) {
      future.addListener(ChannelFutureListener.CLOSE);
    }
  }

  /**
   * 握手动作
   *
   * @param ctx
   * @param msg
   */
  public void shakeHands(ChannelHandlerContext ctx, FullHttpRequest msg) {
    WebSocketServerHandshakerFactory wsFactory =
        new WebSocketServerHandshakerFactory(this.getWebSocketLocation(msg), null, false);
    handshaker = wsFactory.newHandshaker(msg);
    if (handshaker == null) {
      WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
    } else {
      handshaker.handshake(ctx.channel(), msg);
      storeChannel(ctx, issuedEntity);
    }
  }

  /**
   * 构造握手地址
   *
   * @param msg
   * @return
   */
  private String getWebSocketLocation(FullHttpRequest msg) {
    HttpRequest request = msg;
    String url = "ws://" + request.headers().get(HttpHeaders.Names.HOST) + request.uri();
    log.info(url);
    if (url != null) return url;
    return null;
  }

  /**
   * 检验token
   *
   * @param token 传递过来的token
   * @return
   */
  private RepayEntity<Map<String, Object>> verifyToken(String token) {
    com.warpfuture.entity.RepayEntity<Map<String, Object>> repayEntity =
        oauthService.verifyJwt(token);
    return repayEntity;
  }

  /**
   * 用户鉴权
   *
   * @param issuedEntity
   * @return
   */
  private RepayEntity<Boolean> verifyUserToDev(IssuedEntity issuedEntity) {
    String applicationId = (String) issuedEntity.getSource().get("applicationId");
    String userId = (String) issuedEntity.getSource().get("userId");
    String productionId = (String) issuedEntity.getTarget().get("productionId");
    String deviceId = (String) issuedEntity.getTarget().get("deviceId");

    if (applicationId != null && userId != null && productionId != null && deviceId != null) {
      ProtocolRouteDto protocolRouteDto = new ProtocolRouteDto();
      protocolRouteDto.setApplicationId(applicationId);
      protocolRouteDto.setDeviceId(deviceId);
      protocolRouteDto.setProductionId(productionId);
      protocolRouteDto.setUserId(userId);
      RepayEntity<Boolean> repay = deviceService.userToDev(protocolRouteDto);
      if (repay != null) return repay;
    }
    return null;
  }

  /**
   * 配置用户订阅的设备
   *
   * @param userId
   * @param productionId
   * @param deviceId
   * @return
   */
  private void storeUd(String userId, String productionId, String deviceId) {
    if (userId != null && productionId != null && deviceId != null) {
      Map<String, Map<String, String>> map = WSServiceImpl.getUdMap();
      Map<String, String> map1 = map.get(userId);
      if (map1 != null) {
        map1.put(productionId, deviceId);
        map.put(userId, map1);
      } else {
        map1 = new ConcurrentHashMap<>();
        map1.put(productionId, deviceId);
        map.put(userId, map1);
      }
    }
  }

  /**
   * 删除用户订阅的设备
   *
   * @param issuedEntity
   */
  private void removeUd(IssuedEntity issuedEntity) {
    if (issuedEntity != null) {
      Map<String, Object> map = issuedEntity.getSource();
      if (map != null && !map.isEmpty()) {
        String userId = (String) map.get("userId");
        if (WSServiceImpl.getUdMap().containsKey(userId)) {
          WSServiceImpl.getUdMap().remove(userId);
        }
      }
    }
  }

  /**
   * 存储用户连接
   *
   * @param context
   * @param issuedEntity 下发指令实体
   */
  private void storeChannel(ChannelHandlerContext context, IssuedEntity issuedEntity) {
    String accountId = issuedEntity.getAccountId();
    String userId = (String) issuedEntity.getSource().get("userId");
    Map<String, Map<String, Object>> map = WSServiceImpl.getChannelMap();
    log.info("存储用户连接前的wsMap:{}", map);
    if (accountId != null && userId != null) {
      if (map.containsKey(accountId)) {
        Map<String, Object> map1 = map.get(accountId);
        if (map1 != null) {
          List<Channel> channelList = (List<Channel>) map1.get(userId);
          if (channelList != null) {
            channelList.add(context.channel());
          } else {
            channelList = Collections.synchronizedList(new ArrayList<>());
            channelList.add(context.channel());
            map1.put(userId, channelList);
          }
        }
      } else {
        Map<String, Object> map1 = new ConcurrentHashMap<>();
        List<Channel> channelList = Collections.synchronizedList(new ArrayList<>());
        channelList.add(context.channel());
        map1.put(userId, channelList);
        map.put(accountId, map1);
      }
    }
    log.info("存储用户连接后的wsMap:{}", map);
  }

  private void removeChannel(ChannelHandlerContext context, IssuedEntity issuedEntity) {
    String accountId = issuedEntity.getAccountId();
    String userId = null;
    if (issuedEntity.getSource() != null) {
      userId = (String) issuedEntity.getSource().get("userId");
    }
    Map<String, Map<String, Object>> map = WSServiceImpl.getChannelMap();
    if (accountId != null && userId != null) {
      Map<String, Object> map1 = map.get(accountId);
      List<Channel> channelList = (List<Channel>) map1.get(userId);
      if (channelList != null) {
        if (channelList.contains(context.channel())) {
          channelList.remove(context.channel());
        }
        if (channelList.size() == 1) {
          map1.remove(userId);
        }
      }
    }
  }
}
