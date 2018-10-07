package com.warpfuture.iot.protocol.service.impl;

import com.warpfuture.iot.protocol.handler.WebSocketHandler;
import com.warpfuture.iot.protocol.service.ProducerService;
import com.warpfuture.iot.protocol.service.WSService;
import com.warpfuture.service.DeviceService;
import com.warpfuture.service.OauthService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Created by 徐海瀚 on 2018/4/15. */
@Log4j2
@Service
public class WSServiceImpl implements WSService {

  // 创建bootstrap
  private ServerBootstrap serverBootstrap = new ServerBootstrap();
  // BOSS
  private EventLoopGroup boss = new NioEventLoopGroup();
  // Worker
  private EventLoopGroup work = new NioEventLoopGroup();

  @Autowired private DeviceService deviceService;

  @Autowired private ProducerService producerService;

  @Autowired private OauthService oauthService;

  private static Boolean userResponse;

  @Value("${protocol.ws.port}")
  private int port;

  private static Boolean isKillUser; // 在spring config那边配置是否在用户鉴权失败的时候kill掉连接 有可能存在网络原因鉴权不成功
  // 通道管理group
  public static ChannelGroup defaultChannelGroup =
      new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  private static Map<String, Map<String, Object>> wsMap = new ConcurrentHashMap<>();
  private static Map<String, Map<String, String>> udMap = new ConcurrentHashMap<>(); // 用户关注某个设备映射表

  /** 开启及服务线程 */
  @PostConstruct
  @Override
  public void start() throws InterruptedException {
    serverBootstrap.group(boss, work);
    serverBootstrap
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .option(ChannelOption.TCP_NODELAY, true)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .handler(new LoggingHandler(LogLevel.INFO));
    new Thread(
            () -> {
              try {
                serverBootstrap.childHandler(new ChildChannelHandler());
                log.info("WS服务在[{}]端口启动监听", port);
                ChannelFuture f = serverBootstrap.bind(port).sync();
                if (f.isSuccess()) {
                  log.info("WebSocket启动成功");
                } else {
                  log.error("WebSocket开始失败");
                }
                f.channel().closeFuture().sync();
              } catch (InterruptedException e) {
                e.printStackTrace();
                boss.shutdownGracefully();
                work.shutdownGracefully();
              }
            })
        .start();
  }

  /** 关闭服务器方法 */
  @PreDestroy
  @Override
  public void close() {
    // 优雅退出
    boss.shutdownGracefully();
    work.shutdownGracefully();
  }

  public static Map<String, Map<String, Object>> getChannelMap() {
    return wsMap;
  }

  public static Boolean getIsKillUser() {
    return isKillUser;
  }

  @Value("${wf.iot.ws.isKillUser}")
  public void setIsKillUser(Boolean isKillUser) {
    WSServiceImpl.isKillUser = isKillUser;
  }

  public static Map<String, Map<String, String>> getUdMap() {
    return udMap;
  }

  public static ChannelGroup getDefaultChannelGroup() {
    return defaultChannelGroup;
  }

  public static Boolean getUserResponse() {
    return userResponse;
  }

  @Value("${wf.iot.ws.userResponse}")
  public void setUserResponse(Boolean userResponse) {
    WSServiceImpl.userResponse = userResponse;
  }

  private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

      ChannelPipeline pipeline = ch.pipeline();
      pipeline.addLast("http-codec", new HttpServerCodec());
      pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
      pipeline.addLast("http-chunked", new ChunkedWriteHandler());
      pipeline.addLast(
          "handler", new WebSocketHandler(deviceService, oauthService, producerService));
    }
  }
}
