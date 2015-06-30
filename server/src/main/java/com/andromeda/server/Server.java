package com.andromeda.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import static java.util.stream.Collectors.toList;

final class Server {
  public Server(Collection<EndpointOptions> listeners) {
    this.endpointOptions = listeners;
  }

  public synchronized void start() throws InterruptedException {
    createEndpoints(endpointOptions).forEach(this::bindEndpoint);

    while (isRunning.get()) {
      try {
        synchronized (isRunning) {
          isRunning.wait();
        }
      } catch (InterruptedException ignored) {}
    }
  }

  private List<ServerEndpoint> createEndpoints(Collection<EndpointOptions> options) {
    return options.stream().map(
        opt -> new ServerEndpoint(
            opt,
            new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(getChannelInitializer(opt.useSsl))
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
        )
    ).collect(toList());
  }

  private void bindEndpoint(ServerEndpoint ep) {
    ep.bootstrap
        .bind(ep.options.port)
        .addListener(onEndpointBound(ep));
  }

  private ChannelFutureListener onEndpointBound(ServerEndpoint ep) {
    return binding -> {
      if (binding.isSuccess()) {
        endpoints.add(ep);
        binding.channel().closeFuture().addListener(onEndpointClosed(ep));
        log.info("bound " + ep);
      } else {
        log.error("exception binding " + ep, binding.cause());
      }
    };
  }

  private ChannelFutureListener onEndpointClosed(ServerEndpoint ep) {
    return closed -> {
      log.info("closed: " + ep);
      if (endpoints.remove(ep) && endpoints.isEmpty())
          shutdown();
    };
  }

  private void shutdown() {
    log.info("stopping work pools");
    workerGroup.shutdownGracefully();
    bossGroup.shutdownGracefully();

    synchronized (isRunning) {
      isRunning.set(false);
      isRunning.notify();
    }
  }

  private ChannelInitializer getChannelInitializer(boolean useSsl) {
    return new ServerInitializer(useSsl ? getSslContext() : null);
  }

  private SslContext getSslContext() {
    if (sslCtx == null) {
      try {
        SelfSignedCertificate cert = new SelfSignedCertificate();
        sslCtx = SslContext.newServerContext(cert.certificate(), cert.privateKey());
      } catch (CertificateException | SSLException e) {
        log.error("exception configuring SSL", e);
      }
    }
    return sslCtx;
  }

  private final AtomicBoolean isRunning = new AtomicBoolean(true);

  private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  private final EventLoopGroup workerGroup = new NioEventLoopGroup();

  private final Collection<EndpointOptions> endpointOptions;
  private final Set<ServerEndpoint> endpoints = new HashSet<>();

  private SslContext sslCtx = null;

  private static final Logger log = LoggerFactory.getLogger(Server.class);
}
