package com.andromeda.world.galaxy;

import com.andromeda.world.map.AutoMapLoader;
import com.andromeda.world.map.ConcurrentTileMap;
import com.andromeda.world.map.HexMapLayout;
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
    map = ConcurrentTileMap.allocate(HexMapLayout.withDiameter(diameter)).setName("Galaxy");
    mapLoader = new AutoMapLoader(map);
  }

  public TileMap getTiles() {
    return mapLoader;
  }

  private final long seed;
  private final TileMap map;
  private final AutoMapLoader mapLoader;
}
