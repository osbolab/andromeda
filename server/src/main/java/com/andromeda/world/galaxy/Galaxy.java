package com.andromeda.world.galaxy;

import com.andromeda.world.map.HexMapLayout;
import com.andromeda.world.map.MemoryTileMap;
import com.andromeda.world.map.TileMap;
import com.typesafe.config.Config;


public class Galaxy {
  public static final class Layer {
    public static final int BlackHoles = 0;
    public static final int Systems = 1;
  }

  public static Galaxy configure(final Config conf) {
    return new Galaxy(conf.getLong("seed"), conf.getInt("diameter"));
  }

  private Galaxy(long seed, int diameter) {
    this.seed = seed;
    map = MemoryTileMap.allocate(HexMapLayout.withDiameter(diameter));
  }

  public TileMap map() {
    return map;
  }

  private final long seed;
  private final TileMap map;
}
