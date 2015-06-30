package com.andromeda.client;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
  public static void main(String[] args) throws Exception {
    final Config clientConf = ConfigFactory.load().getConfig("andromeda.client");

    final InetSocketAddress serverAddress = new InetSocketAddress(
        clientConf.getString("connect.address"),
        clientConf.getInt("connect.port")
    );

    EventLoopGroup workerGroup = new NioEventLoopGroup(1);
    try {
      Bootstrap client = new Bootstrap()
          .group(workerGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new ServerChannelFactory());

      log.info("connecting to {}:{}", serverAddress.getHostName(), serverAddress.getPort());

      client.connect(serverAddress).sync()
          .channel().closeFuture().sync();

      log.info("disconnected");

    } finally {
      log.debug("stopping connection pool");
      workerGroup.shutdownGracefully();
    }
  }

  private static final Logger log = LoggerFactory.getLogger(ClientMain.class);
}
