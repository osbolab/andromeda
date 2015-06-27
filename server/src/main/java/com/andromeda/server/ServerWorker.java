package com.andromeda.server;

import com.andromeda.net.Messages.TestMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;

final class ServerWorker extends SimpleChannelInboundHandler<TestMessage> {
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.pipeline()
       .get(SslHandler.class)
       .handshakeFuture()
       .addListener(future -> {
         log.info(ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite());

         TestMessage.Builder builder = TestMessage.newBuilder();
         builder.setMessage("A N D R O M E D A");

         final ChannelFuture f = ctx.writeAndFlush(builder.build());
         // Write is asynchronous, so wait for it to finish before closing
         f.addListener(new ChannelFutureListener() {
           @Override
           public void operationComplete(ChannelFuture future) throws Exception {
             assert f == future;
             ctx.close();
           }
         });
       });
  }

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, TestMessage msg) throws Exception {
    log.info(msg.getMessage());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("exception handling client channel", cause);
    ctx.close();
  }

  private static final Logger log = LoggerFactory.getLogger(ServerWorker.class);
}
