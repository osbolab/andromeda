package com.andromeda.game.map;

import java.util.HashMap;
import java.util.Map;

public final class TileMap<T> {
  public TileMap(MapLayout layout) {
    this.layout = layout;
    map = new HashMap<>(layout.getMaxTileCount());
  }

  public Tile<T> at(int x, int y) {
    return new Tile<>(this, x, y);
  }

  public Tile<T> neighbor(int x, int y, int direction) {
    final Coord2 dir = Direction.coords[direction];
    return at(x + dir.x, y + dir.y);
  }

  MapLayout getLayout() {
    return layout;
  }

  T get(int index) {
    return map.get(index);
  }

  T set(int index, T contents) {
    return map.put(index, contents);
  }

  T remove(int index) {
    return map.remove(index);
  }

  private final MapLayout layout;
  private final Map<Integer, T> map;

  public final TileSelectors<T> select = new TileSelectors<T>() {
    @Override
    public Tiles<T> radius(int x, int y, int radius) {
      return new RadiusSelector<>(TileMap.this, x, y, radius);
    }

    @Override
    public Tiles<T> ring(int x, int y, int radius) {
      return new RingSelector<>(TileMap.this, x, y, radius);
    }
  };
}
