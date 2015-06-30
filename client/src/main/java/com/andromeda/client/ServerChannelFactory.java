package com.andromeda.client;

import com.andromeda.net.Messages.TestMessage;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ServerChannelFactory extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ch.pipeline()
        .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
        .addLast("protobufDecoder", new ProtobufDecoder(TestMessage.getDefaultInstance()))

        .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
        .addLast("protobufEncoder", new ProtobufEncoder())

        .addLast(new ServerChannelWorker());
  }
}
