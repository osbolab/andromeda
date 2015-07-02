package com.andromeda.server;

import com.typesafe.config.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


final class Server {

  static Server configure(final Config conf,
                          EventLoopGroup listenerGroup,
                          EventLoopGroup clientGroup) {
    return new Server(
        new InetSocketAddress(conf.getString("listen.address"), conf.getInt("listen.port")),
        new ServerBootstrap()
            .group(listenerGroup, clientGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ClientChannelFactory())
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
    );
  }

  private Server(final InetSocketAddress listenAddress, final ServerBootstrap bootstrap) {
    this.listenAddress = listenAddress;
    this.bootstrap = bootstrap;
  }

  public synchronized void listenForever() throws InterruptedException {
    final ChannelFuture bound = bootstrap.bind(listenAddress).sync();
    log.info("listening on " + listenAddress.getHostName() + ":" + listenAddress.getPort());
    bound.channel().closeFuture().sync();
  }

  private final ServerBootstrap bootstrap;
  private final InetSocketAddress listenAddress;

  private static final Logger log = LoggerFactory.getLogger(Server.class);
}
