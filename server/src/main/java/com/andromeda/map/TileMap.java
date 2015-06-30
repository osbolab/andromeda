package com.andromeda.map;

import java.util.HashMap;
import java.util.Map;

public class TileMap<C> {
  public TileMap(MapLayout layout) {
    this.layout = layout;
    map = new HashMap<>(layout.getMaxTileCount());
  }

  private final MapLayout layout;
  private final Map<Integer, Tile<C>> map;
}
