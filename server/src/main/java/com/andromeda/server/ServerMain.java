package com.andromeda.server;

import com.typesafe.config.ConfigFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;


public class ServerMain {

  public static void main(String[] args) throws Exception {
    try {
      Server.configure(
          ConfigFactory.load().getConfig("andromeda.server"),
          listenerGroup, clientGroup
      ).listenForever();

    } finally {
      listenerGroup.shutdownGracefully();
      clientGroup.shutdownGracefully();
    }
  }

  private static final EventLoopGroup listenerGroup = new NioEventLoopGroup(1);
  private static final EventLoopGroup clientGroup = new NioEventLoopGroup();

  private static final Logger log = LoggerFactory.getLogger(ServerMain.class);
}
