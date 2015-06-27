package com.andromeda.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

final class Server {
  public Server(int port) {
    this.port = port;
  }

  public void start() throws InterruptedException {
    SslContext sslCtx = null;
    try {
      SelfSignedCertificate cert = new SelfSignedCertificate();
      sslCtx = SslContext.newServerContext(cert.certificate(), cert.privateKey());
    } catch (CertificateException | SSLException e) {
      log.error("exception configuring SSL", e);
    }

    log.info("starting server on port {} ({})",
             port, (sslCtx != null) ? "secure" : "unsecured");

    EventLoopGroup listenerGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap server = new ServerBootstrap();
      server.group(listenerGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ServerInitializer(sslCtx))
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);

      // Start accepting connections and delegating them to the client handler
      ChannelFuture f = server.bind(port).sync();
      // -- wait for channel to close --

      f.channel().closeFuture().sync();

    } finally {
      workerGroup.shutdownGracefully();
      listenerGroup.shutdownGracefully();
    }
  }

  private static final Logger log = LoggerFactory.getLogger(Server.class);
  private final int port;
}
