package com.andromeda.server;

import io.netty.bootstrap.ServerBootstrap;

final class ServerEndpoint {
  ServerEndpoint(EndpointOptions options, ServerBootstrap bootstrap) {
    this.options = options;
    this.bootstrap = bootstrap;
  }

  @Override
  public String toString() {
    return "ServerEndpoint(port: " + options.port + ", ssl: " + options.useSsl + ")";
  }

  final EndpointOptions options;
  final ServerBootstrap bootstrap;
}
