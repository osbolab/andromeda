package com.andromeda.client;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
  public static void main(String[] args) throws Exception {
    final Config clientConf = ConfigFactory.load().getConfig("andromeda.client");

    final RemoteEndpoint server = RemoteEndpoint.configure(clientConf.getConfig("connect"));

    log.info("connecting to {}", server);

    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      List<ChannelFuture> futures = new ArrayList<>();

      // Start all clients connecting
      for (int i = 0; i < 100; ++i) {
        Bootstrap client = new Bootstrap()
            .group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(new ClientInitializer(server));

        futures.add(client.connect(server.getAddress()));
      }

      // Wait for a client to connect, then wait for it to close.
      futures.forEach(connecting ->
                          connecting.syncUninterruptibly()
                              .channel()
                              .closeFuture().syncUninterruptibly()
      );

    } finally {
      workerGroup.shutdownGracefully();
    }
  }

  private static final Logger log = LoggerFactory.getLogger(ClientMain.class);
}
