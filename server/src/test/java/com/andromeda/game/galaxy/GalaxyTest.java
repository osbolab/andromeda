package com.andromeda.game.galaxy;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import com.andromeda.world.map.FutureTiles;
import com.andromeda.world.map.Tile;
import com.andromeda.world.map.Tiles;
import com.andromeda.world.galaxy.Galaxy;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.junit.Test;


public final class GalaxyTest {
  @Test
  public void demonstrateUsage() {
    Galaxy gal = Galaxy.configure(galConf);

    FutureTiles loading = gal.map().select(Galaxy.Layer.Systems).inRadius(0, 0, 1);
    // hasNext() blocks until true or finished
    for (Tile tile : loading) {
    }

    // Block until available
    // Tiles tiles = loading.get();

    // Wait for all tiles to be available
    Futures.addCallback(loading, new FutureCallback<Tiles>() {
      @Override
      public void onSuccess(Tiles tiles) {

      }

      @Override
      public void onFailure(Throwable t) {
      }
    });
  }

  private static final Config galConf = ConfigFactory.load().getConfig("andromeda.world.galaxy");
}
