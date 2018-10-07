package com.warpfuture.iot.protocol.service.impl;

import com.warpfuture.iot.protocol.handler.WTTHandler;
import com.warpfuture.iot.protocol.service.ProducerService;
import com.warpfuture.iot.protocol.service.WTTService;
import com.warpfuture.service.DeviceService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/** Created by 徐海瀚 on 2018/4/12. */
@Log4j2
@Service
public class WTTServiceImpl implements WTTService {

  // 通道管理group
  public static ChannelGroup defaultChannelGroup =
      new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  private static Map<String, Map<String, Channel>> wttMap = new ConcurrentHashMap<>();
  // 创建bootstrap
  private ServerBootstrap serverBootstrap = new ServerBootstrap();
  // BOSS
  private EventLoopGroup boss = new NioEventLoopGroup();
  // Worker
  private EventLoopGroup work = new NioEventLoopGroup();
  @Autowired private ProducerService producerService;
  @Autowired private DeviceService deviceService;

  @Value("${protocol.wtt.port}")
  private int port;

  private static Boolean screen;

  @Value("${protocol.wtt.screen}")
  public void setScreen(Boolean screen) {
    WTTServiceImpl.screen = screen;
  }

  public static Boolean getScreen() {
    return screen;
  }

  public static void setScreenValue(Boolean screen) {
    WTTServiceImpl.screen = screen;
  }

  public static Map<String, Map<String, Channel>> getChannelMap() {
    return wttMap;
  }

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
                log.info("wtt服务在[{}]端口启动监听", port);
                ChannelFuture f = serverBootstrap.bind(port).sync();
                if (f.isSuccess()) {
                  log.info("长连接启动成功");
                } else {
                  log.error("长连接开始失败");
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

  private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
      ChannelPipeline pipeline = ch.pipeline();
      pipeline.addLast("idleStateHandler", new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
      pipeline.addLast("lineBasedFrameDecoder", new LineBasedFrameDecoder(1024, false, true));
      pipeline.addLast("loginAuthHandler", new WTTHandler(producerService, deviceService));
    }
  }
}
