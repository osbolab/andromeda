package com.andromeda.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

final class TimeClient {
  public static void main(String[] args) throws Exception {
    final String host = "127.0.0.1";
    final int port = 9999;

    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      Bootstrap client = new Bootstrap()
          .group(workerGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(channelHandler());

      ChannelFuture f = client.connect(host, port).sync();
      // -- wait for channel to close --

      f.channel().closeFuture().sync();

    } finally {
      workerGroup.shutdownGracefully();
    }
  }

  private static ChannelHandler channelHandler() {
    return new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ClientWorker());
      }
    };
  }

  private static final class ClientWorker extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      final ByteBuf buf = (ByteBuf) msg;
      try {
        long currentTimeMs = (buf.readUnsignedInt() - 2208988800L) * 1000L;
        log.info("the time is {}", new Date(currentTimeMs));

      } finally {
        buf.release();
      }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      log.error("exception handling channel", cause);
      ctx.close();
    }

    private static final Logger log = LoggerFactory.getLogger(ClientWorker.class);
  }
}
