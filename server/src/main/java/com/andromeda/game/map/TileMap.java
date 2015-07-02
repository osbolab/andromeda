package com.andromeda.game.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public final class TileMap<T> {
  public static <T> TileMap<T> allocate(MapLayout mapLayout) {
    return new TileMap<>(mapLayout);
  }

  private TileMap(MapLayout layout) {
    Objects.requireNonNull(layout);

    this.layout = layout;
    map = new HashMap<>(layout.getMaxTileCount(), 0.75f);
  }

  boolean contains(int x, int y) {
    return layout.contains(x, y);
  }

  boolean containsRadius(int x, int y, int radius) {
    return layout.containsRadius(x, y, radius);
  }

  int indexOf(int x, int y) {
    return layout.indexOf(x, y);
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
    public Tile<T> at(int x, int y) {
      return new Tile<>(TileMap.this, x, y);
    }

    public Tile<T> neighbor(int x, int y, int direction) {
      assert direction < Direction.coords.length : "Direction index out of bounds";
      final Coord2 dir = Direction.coords[direction];
      return at(x + dir.x, y + dir.y);
    }

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
