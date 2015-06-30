package com.andromeda.client;

import com.typesafe.config.Config;

import java.net.InetSocketAddress;

import javax.annotation.Nullable;
import javax.net.ssl.SSLException;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

final class RemoteEndpoint {
  static RemoteEndpoint configure(final Config conf) throws SSLException {
    final String host = conf.getString("host");
    if (conf.getBoolean("useSsl")) {
      return new RemoteEndpoint(new InetSocketAddress(host, conf.getInt("sslPort")), true);
    } else {
      return new RemoteEndpoint(new InetSocketAddress(host, conf.getInt("port")));
    }
  }


  public RemoteEndpoint(InetSocketAddress endpoint, boolean useSsl) {
    this.endpoint = endpoint;
    this.useSsl = useSsl;
  }

  private RemoteEndpoint(InetSocketAddress endpoint) {
    this(endpoint, false);
  }

  public InetSocketAddress getAddress() {
    return endpoint;
  }

  public String toString() {
    return endpoint.toString() + (useSsl ? " (secure)" : "");
  }

  @Nullable
  public SslContext getSslContext() throws SSLException {
    if (useSsl)
      return SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
    else
      return null;
  }

  private final boolean useSsl;
  private final InetSocketAddress endpoint;
}
