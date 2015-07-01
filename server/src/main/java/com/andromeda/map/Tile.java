package com.andromeda.map;

public final class Tile<T> {
  Tile(TileMap<T> map, int x, int y) {
    this.map = map;
    this.x = x;
    this.y = y;
    index = map.getLayout().indexOf(x, y);
  }

  public boolean exists() {
    return map.get(index) != null;
  }

  public T get() {
    return map.get(index);
  }

  public T set(T contents) {
    return map.set(index, contents);
  }

  public T remove() {
    return map.remove(index);
  }

  public Tile<T> getNeighbor(int direction) {
    return map.neighbor(x, y, direction);
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tile<?> tile = (Tile<?>) o;
    return index == tile.index && map.equals(tile.map);
  }

  @Override
  public int hashCode() {
    return 31 * map.hashCode() + index;
  }

  private final TileMap<T> map;
  public final int x;
  public final int y;
  private final int index;
}
