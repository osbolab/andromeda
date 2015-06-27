package com.andromeda.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
  public static void main(String[] args) throws Exception {
    int port = 0;
    if (args.length >= 1)
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException logged) {
        log.warn("invalid port specified: " + args[0]);
      }

    if (port <= 0)
      port = 9999;

    new Server(port).start();
  }

  private static final Logger log = LoggerFactory.getLogger(ServerMain.class);
}
