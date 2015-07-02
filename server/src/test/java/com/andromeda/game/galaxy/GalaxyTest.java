package com.andromeda.game.galaxy;

import com.andromeda.world.galaxy.Galaxy;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.junit.Test;


public final class GalaxyTest {
  @Test
  public void generateLayer0() {
    Galaxy gal = Galaxy.configure(galConf);
  }

  private static final Config galConf = ConfigFactory.load().getConfig("andromeda.world.galaxy");
}
