package com.andromeda.server;

import com.andromeda.net.Messages.TestMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;


final class ClientChannelWorker extends SimpleChannelInboundHandler<TestMessage> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    SslHandler sslCtx = ctx.pipeline().get(SslHandler.class);
    if (sslCtx != null) {
      sslCtx.handshakeFuture()
          .addListener(f -> {
            log.info(sslCtx.engine().getSession().getCipherSuite());
            onConnected(ctx);
          });
    } else {
      onConnected(ctx);
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
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

  private void onConnected(ChannelHandlerContext ctx) {
    TestMessage.Builder msg = TestMessage.newBuilder();
    msg.setMessage("A N D R O M E D A");

    ctx.writeAndFlush(msg.build());
  }

  private static final Logger log = LoggerFactory.getLogger(ClientChannelWorker.class);
}
