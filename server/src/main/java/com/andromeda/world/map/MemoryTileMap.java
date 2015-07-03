package com.andromeda.world.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class MemoryTileMap implements TileMap {
  public static MemoryTileMap allocate(MapLayout layout) {
    return new MemoryTileMap(layout,
                             new ConcurrentHashMap<>(layout.getMaxTileCount(), 1.0f));
  }

  private MemoryTileMap(MapLayout layout, Map<Integer, TileData> map) {
    this.layout = layout;
    this.map = map;
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
  private final Map<Integer, TileData> map;
}
