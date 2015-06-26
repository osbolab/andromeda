package com.andromeda.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  public static void main(String[] args) throws Exception {
    int port = 0;
    if (args.length > 0) {
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException ignored) {}
    }

    if (port <= 0)
      port = 9999;

    log.info("accepting clients on port {}", port);
    new Server(port).start();
  }

  private static final Logger log = LoggerFactory.getLogger(Main.class);
}
