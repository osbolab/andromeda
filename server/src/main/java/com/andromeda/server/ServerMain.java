package com.andromeda.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ServerMain {
  public static void main(String[] args) throws Exception {
    final Config serverConf = ConfigFactory.load().getConfig("andromeda.server");

    final List<EndpointOptions> listeners = serverConf
        .getConfigList("endpoints").stream()
        .map(
            conf -> new EndpointOptions(
                conf.getInt("port"),
                conf.getBoolean("useSsl")
            )
        ).collect(Collectors.toList());

    final int maxRestarts = Math.max(0, serverConf.getInt("keep-alive.max-restarts"));
    while (launches++ <= maxRestarts) {
      try {
        if (launches > 1)
          log.warn("restarting server");
        new Server(listeners).start();

      } catch (Throwable t) {
        log.error("server crashed due to unhandled exception", t);
      }
    }
  }

  private static final Logger log = LoggerFactory.getLogger(ServerMain.class);
  private static int launches = 0;
}
