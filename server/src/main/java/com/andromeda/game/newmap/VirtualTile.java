package com.andromeda.game.newmap;

/**
 * A virtual tile doesn't exist on a particular map; instead it behaves as if existing on an
 * unbounded map.
 */
public class VirtualTile {
  public VirtualTile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Tile realize(TileMap in) {
    return new Tile(x, y, in);
  }

  public final int x;
  public final int y;
}
