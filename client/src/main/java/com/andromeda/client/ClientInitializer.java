package com.andromeda.client;

import com.andromeda.net.Messages.TestMessage;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
  public ClientInitializer(RemoteEndpoint server) {
    this.server = server;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    final SslContext sslCtx = server.getSslContext();
    if (sslCtx != null) {
      final InetSocketAddress host = server.getAddress();
      final ChannelHandler sslHandler = sslCtx.newHandler(ch.alloc(),
                                                          host.getHostName(),
                                                          host.getPort());
      pipeline.addLast("ssl", sslHandler);
    }

    pipeline
        .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
        .addLast("protobufDecoder", new ProtobufDecoder(TestMessage.getDefaultInstance()))

        .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
        .addLast("protobufEncoder", new ProtobufEncoder())

        .addLast(new ClientWorker());
  }

  private final RemoteEndpoint server;
}
