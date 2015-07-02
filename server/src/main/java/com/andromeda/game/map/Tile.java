package com.andromeda.game.map;

/**
 * References a specific real tile in a particular map.
 * <p>
 * Thread safety: access to mutable state in this class is synchronized, but accessors are delegated
 * to {@link TileMap} and their thread safety depends on the implementation provided.
 */
public final class Tile<T> {
  Tile(TileMap<T> map, int x, int y) {
    this.map = map;
    this.x = x;
    this.y = y;
    key = map.toKey(x, y);
  }

  /**
   * Get the unique value in the range (-{@link Long#MIN_VALUE}, {@link Long#MAX_VALUE}) associated
   * with this tile's coordinates.
   * <p>
   * This value is used to produce unique sequences for each tile from random number generators.
   * Using the tile's coordinates ensures that generated sequences depend on the tile's world
   * properties instead of on the storage implementation.
   */
  public long getSeed() {
    return ((long) x << 32) + y;
  }

  /** Return {@code true} if the tile has an attached object reference. */
  public boolean exists() {
    return map.get(key) != null;
  }

  /** @see TileMap#get(int) */
  public T get() {
    return map.get(key);
  }

  /** @see TileMap#set(int, Object) */
  public T set(T contents) {
    return map.set(key, contents);
  }

  /** @see TileMap#remove(int) */
  public T remove() {
    return map.remove(key);
  }

  /** @see TileSelectors#neighbor(int, int, int) */
  public Tile<T> neighbor(int direction) {
    return map.tiles().neighbor(x, y, direction);
  }

  /** @see TileSelectors#inRadius(int, int, int) */
  public Tiles<T> neighbors(int radius) {
    return map.tiles().inRadius(x, y, radius);
  }

  public int distanceTo(Tile<T> tile) {
    return Coord2.getDistance(x, y, tile.x, tile.y);
  }

  @Override
  public String toString() {
    return map.getName() + "(" + x + ", " + y + ")";
  }

  /**
   * Tiles are equal iff they refer to the same coordinates in the same {@link TileMap} instance.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tile<?> tile = (Tile<?>) o;
    return key == tile.key && map.equals(tile.map);
  }

  @Override
  public int hashCode() {
    return 31 * map.hashCode() + key;
  }

  /** Get the index of the maximum layer to which this tile has been generated. */
  public int getMaxLayer() {
    synchronized (lock) {
      return maxLayer;
    }
  }

  public void promoteToLayer(int layer) {
    assert layer >= 0 && layer <= Byte.MAX_VALUE;
    synchronized (lock) {
      if (layer > maxLayer)
        maxLayer = (byte) layer;
    }
  }

  private final TileMap<T> map;
  public final int x;
  public final int y;
  private final int key;
  private byte maxLayer = -1;
  private final Object lock = new Object();
}
