package com.andromeda.server;

import com.andromeda.util.release;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

final class ServerWorker extends ChannelHandlerAdapter {
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    final int time = (int) (System.currentTimeMillis() / 1000L + 2208988800L);
    log.info(new Date(System.currentTimeMillis()).toString());

    final ByteBuf timeBuf = ctx.alloc().buffer(4);
    timeBuf.writeInt(time);

    final ChannelFuture f = ctx.writeAndFlush(timeBuf);
    // Write is asynchronous, so wait for it to finish before closing
    f.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        assert f == future;
        ctx.close();
      }
    });
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    release.after((ByteBuf) msg, buf -> echo(buf, ctx));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("exception handling client channel", cause);
    ctx.close();
  }

  private void echo(ByteBuf buf, ChannelHandlerContext ctx) {
    String data = buf.toString(CHARSET).trim();
    log.info(data);

    data = "echo: " + data + '\n';

    byte[] dataBytes = data.getBytes(CHARSET);
    ByteBuf newBuf = ctx.alloc().directBuffer(dataBytes.length);
    newBuf.writeBytes(dataBytes);

    ctx.writeAndFlush(newBuf);
  }

  private static final Charset CHARSET = Charset.forName("UTF-8");
  private static final Logger log = LoggerFactory.getLogger(ServerWorker.class);
}
