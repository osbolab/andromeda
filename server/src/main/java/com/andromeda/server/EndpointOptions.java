package com.andromeda.server;

final class EndpointOptions {
  EndpointOptions(int port, boolean useSsl) {
    if (port <= 0)
      throw new IllegalArgumentException("listen port must be positive");
    this.port = port;
    this.useSsl = useSsl;
  }

  final int port;
  final boolean useSsl;
}
