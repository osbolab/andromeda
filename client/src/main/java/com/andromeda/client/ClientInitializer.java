package com.andromeda.client;

import com.andromeda.net.Messages.TestMessage;

import java.net.InetSocketAddress;

import javax.annotation.Nullable;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
  public ClientInitializer(@Nullable SslContext sslCtx, InetSocketAddress host) {
    this.sslCtx = sslCtx;
    this.host = host;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    if (sslCtx != null)
      pipeline.addLast("ssl", sslCtx.newHandler(ch.alloc(), host.getHostName(), host.getPort()));

    pipeline
        .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
        .addLast("protobufDecoder", new ProtobufDecoder(TestMessage.getDefaultInstance()))

        .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
        .addLast("protobufEncoder", new ProtobufEncoder())

        .addLast(new ClientWorker());
  }

  private final SslContext sslCtx;
  private final InetSocketAddress host;
}
