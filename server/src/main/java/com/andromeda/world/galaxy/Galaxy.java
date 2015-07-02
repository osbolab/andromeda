package com.andromeda.world.galaxy;

import com.andromeda.game.map.HexMapLayout;
import com.andromeda.game.map.ConcurrentTileMap;
import com.typesafe.config.Config;


public class Galaxy {
  public static Galaxy configure(final Config conf) {
    return new Galaxy(conf.getLong("seed"), conf.getInt("diameter"));
  }

  private Galaxy(long seed, int diameter) {
    this.seed = seed;
    map = ConcurrentTileMap.allocate(HexMapLayout.withDiameter(diameter));
  }

  long getSeed() {
    return seed;
  }

  ConcurrentTileMap<GalaxyTile> getMap() {
    return map;
  }

  private final long seed;
  private final ConcurrentTileMap<GalaxyTile> map;
}
