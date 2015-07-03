package com.andromeda.game.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


final class MemoryTileMap implements TileMap {
  private MemoryTileMap(MapLayout layout) {
    this.layout = layout;
    map = new ConcurrentHashMap<>(layout.getMaxTileCount(), 1.0f);
  }

  @Override
  public int toKey(int x, int y) {
    return layout.toKey(x, y);
  }

  @Override
  public TileData get(int key) {
    return map.computeIfAbsent(key, k -> new TileData());
  }

  private final MapLayout layout;
  private final ConcurrentMap<Integer, TileData> map;
}
