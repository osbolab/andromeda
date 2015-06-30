package com.andromeda.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

final class Server {
  public synchronized void start(int port) throws InterruptedException {
    try {
      ServerBootstrap bs = new ServerBootstrap()
          .group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ServerInitializer())
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);

      final ChannelFuture bound = bs.bind(port).sync();

      log.info("listening on port " + port);
      bound.channel().closeFuture().sync();

    } finally {
      shutdown();
    }
  }

  private void shutdown() {
    log.info("stopping work pools");
    workerGroup.shutdownGracefully();
    bossGroup.shutdownGracefully();
  }

  private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  private final EventLoopGroup workerGroup = new NioEventLoopGroup();

  private static final Logger log = LoggerFactory.getLogger(Server.class);
}
