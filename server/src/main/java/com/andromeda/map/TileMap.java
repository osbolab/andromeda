package com.andromeda.map;

import java.util.HashMap;
import java.util.Map;

public class TileMap<T> {
  public TileMap(MapLayout layout) {
    this.layout = layout;
    map = new HashMap<>(layout.getMaxTileCount());
  }

  public Tile<T> at(int x, int y) {
    return new Tile<>(this, x, y);
  }

  public T remove(int x, int y) {
    return map.remove(layout.indexOf(x, y));
  }

  public Tile<T> at(int x, int y, int neighbor) {
    final Coord2 direction = directions[neighbor];
    return at(x + direction.x, y + direction.y);
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

  private static final Coord2[] directions = new Coord2[]{
      new Coord2(1, 0), new Coord2(0, 1), new Coord2(-1, 1),
      new Coord2(-1, 0), new Coord2(0, -1), new Coord2(1, -1)
  };
  private final MapLayout layout;
  private final Map<Integer, T> map;

  public final TileSelector<T> select = new TileSelector<T>() {
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
