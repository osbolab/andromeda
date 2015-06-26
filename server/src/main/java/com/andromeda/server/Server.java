package com.andromeda.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

final class Server {
  public Server(int port) {
    this.port = port;
  }

  public void start() throws InterruptedException {
    EventLoopGroup listenerGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap server = new ServerBootstrap();
      server.group(listenerGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(clientHandler())
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

  private ChannelHandler clientHandler() {
    return new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ServerWorker());
      }
    };
  }

  private final int port;
}
