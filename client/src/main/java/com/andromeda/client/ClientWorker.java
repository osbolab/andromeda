package com.andromeda.client;

import com.andromeda.net.Messages.TestMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

final class ClientWorker extends SimpleChannelInboundHandler<TestMessage> {
  @Override
  protected void messageReceived(ChannelHandlerContext ctx, TestMessage msg) throws Exception {
    log.info(msg.getMessage());
    ctx.writeAndFlush(
        TestMessage.newBuilder()
            .setMessage("Hello " + msg.getMessage())
            .build()
    ).addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("exception handling channel", cause);
    ctx.close();
  }

  private static final Logger log = LoggerFactory.getLogger(ClientWorker.class);
}
