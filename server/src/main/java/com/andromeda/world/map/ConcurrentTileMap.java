package com.andromeda.world.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;


public final class ConcurrentTileMap implements TileMap, TileDataCache {
  public static ConcurrentTileMap allocate(HexMapLayout layout) {
    return new ConcurrentTileMap(layout, new ConcurrentHashMap<>(layout.getTileCapacity(), 1.0f));
  }

  private ConcurrentTileMap(MapLayout layout, ConcurrentMap<Integer, TileData> data) {
    this.layout = layout;
    this.data = data;
  }

  @Override
  public MapLayout getLayout() {
    return layout;
  }

  @Override
  public Tile getTile(int x, int y) {
    return new TileImpl(x, y);
  }

  @Override
  public TileSelector inRadius(int x, int y, int radius) {
    return null;
  }

  @Override
  @Nullable
  public TileData get(int key) {
    return data.get(key);
  }

  @Override
  public TileData getOrInit(int key) {
    return data.computeIfAbsent(key, k -> new TileData());
  }

  public ConcurrentTileMap setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String toString() {
    if (name != null && !name.isEmpty())
      return getClass().getSimpleName() + "('" + name + "')";
    return super.toString();
  }

  private String name;
  private final MapLayout layout;
  private final ConcurrentMap<Integer, TileData> data;


  /** Represents the tile at a given coordinate on a specific map. */
  @NotThreadSafe
  private final class TileImpl implements Tile {
    TileImpl(int x, int y) {
      this.x = x;
      this.y = y;
      key = layout.getTileKey(x, y);
    }

    @Override
    public int getX() {
      return x;
    }

    @Override
    public int getY() {
      return y;
    }

    @Override
    public Object get(int layer) {
      final TileData layers = data.get(key);
      return (layers != null) ? layers.get(layer) : null;
    }

    @Override
    public Object set(int layer, Object newData) {
      return getOrInit(key).set(layer, newData);
    }

    @Override
    @Nullable
    public Tile getNeighbor(int direction) {
      final Coord dir = Coord.directions[direction];
      final int xx = x + dir.x;
      final int yy = y + dir.y;
      return layout.contains(xx, yy) ? new TileImpl(xx, yy) : null;
    }

    @Override
    public long getSeed() {
      return ((long) x << 32) + y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TileImpl tile = (TileImpl) o;
      return tile.key == key && tile.getMap() == ConcurrentTileMap.this;
    }

    @Override
    public int hashCode() {
      return 31 * ConcurrentTileMap.this.hashCode() + key;
    }

    @Override
    public String toString() {
      return "(" + x + ", " + y + ")";
    }

    private ConcurrentTileMap getMap() {
      return ConcurrentTileMap.this;
    }

    final int x;
    final int y;
    final int key;
  }
}
