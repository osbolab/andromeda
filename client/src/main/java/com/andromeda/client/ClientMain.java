package com.andromeda.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class ClientMain {
  public static void main(String[] args) throws Exception {
    String hostName = "127.0.0.1";
    int port = 0;

    if (args.length >= 2)
      hostName = args[0];
    if (args.length >= 3)
      try {
        port = Integer.parseInt(args[1]);
      } catch (NumberFormatException logged) {
        log.warn("invalid port specified: " + args[1]);
      }

    if (port <= 0)
      port = 9999;

    SslContext sslCtx = null;
    try {
      sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
    } catch (SSLException e) {
      log.error("exception configuring SSL", e);
    }

    log.info("connecting to {}:{} ({})",
             hostName, port,
             (sslCtx != null) ? "secure" : "unsecured");

    final InetSocketAddress host = new InetSocketAddress(hostName, port);

    EventLoopGroup workerGroup = new NioEventLoopGroup(1);
    try {
      Bootstrap client = new Bootstrap()
          .group(workerGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new ClientInitializer(sslCtx, host));

      ChannelFuture f = client.connect(host).sync();
      // -- wait for channel to close --

      f.channel().closeFuture().sync();

    } finally {
      workerGroup.shutdownGracefully();
    }
  }

  private static final Logger log = LoggerFactory.getLogger(ClientMain.class);
}
