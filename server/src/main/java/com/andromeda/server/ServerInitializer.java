package com.andromeda.server;

import com.andromeda.net.Messages.TestMessage;

import javax.annotation.Nullable;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;

class ServerInitializer extends ChannelInitializer<SocketChannel> {
  ServerInitializer(@Nullable SslContext sslCtx) {
    this.sslCtx = sslCtx;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    if (sslCtx != null)
      pipeline.addLast("ssl", sslCtx.newHandler(ch.alloc()));

    pipeline
      .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
      .addLast("protobufDecoder", new ProtobufDecoder(TestMessage.getDefaultInstance()))

      .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
      .addLast("protobufEncoder", new ProtobufEncoder())

      .addLast(new ServerWorker());
  }

  private final SslContext sslCtx;
}
