package kr.co.bizframe.boot.nio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import kr.co.bizframe.boot.nio.socket.decoder.MDecoder;
import kr.co.bizframe.boot.nio.socket.handler.EchoServerHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class NettyConfig {

  @Value("${netty.socket.port:10993}")
  private int sockerServerPort;

  private ServerBootstrap serverBootstrap = null;

  private EventLoopGroup bossGroup = null;

  private EventLoopGroup workerGroup = null;

  private ChannelFuture channelFuture = null;

  @PostConstruct
  public void startup() throws InterruptedException {

    // TODO 압축 핸들러추가
    bossGroup = new NioEventLoopGroup(); // acceptor
    workerGroup = new NioEventLoopGroup();
    serverBootstrap = new ServerBootstrap();
    serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            // TODO Auto-generated method stub
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new MDecoder());
            pipeline.addLast("handler", new EchoServerHandler());
          }
        }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

    channelFuture = serverBootstrap.bind(sockerServerPort);
    channelFuture.sync();

    log.info("Starting SocketServer on port {}", sockerServerPort);
  }


  @PreDestroy
  public void shutdown() {
    log.info("Shutdown SocketServer on port {}", sockerServerPort);
    workerGroup.shutdownGracefully();
    bossGroup.shutdownGracefully();
  }

}
