package com.andromeda.game.galaxy;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import com.andromeda.world.galaxy.Galaxy;
import com.andromeda.world.map.Tiles;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.junit.Test;


public final class GalaxyTest {
  @Test
  public void getLayer1Async() {
    Galaxy gal = Galaxy.configure(galConf);

    Tiles tiles = gal.getTiles().inRadius(0, 0, 1).now();

    ListenableFuture<Tiles> loading = gal.getTiles()
        .inRadius(0, 0, 1)              // Doesn't have to be entirely contained by the map space
        .require(Galaxy.Layer.Systems)  // Require that the tiles be generated to this layer
        .later();                       // Do the selection and generation in the background

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
