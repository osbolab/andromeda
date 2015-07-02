package com.andromeda.game.map;

public final class VirtualTile<T> {
  public static <T> VirtualTile<T> at(int x, int y) {
    return new VirtualTile<>(x, y);
  }

  private VirtualTile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public VirtualTile<T> neighbor(int direction) {
    final Coord2 dir = Direction.coords[direction];
    return new VirtualTile<>(x + dir.x, y + dir.y);
  }

  public Tile<T> realize(TileMap<T> map) {
    return map.select.at(x, y);
  }

  public final int x;
  public final int y;
}
