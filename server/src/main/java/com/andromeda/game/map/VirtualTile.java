package com.andromeda.game.map;

/**
 * A virtual tile refers to a 2D coordinate in an unspecified tessellation with unspecified
 * geometry. This may or may not be a real tile in any given {@link TileMap}.
 * <p>
 * Instances may be used to express functions that transiently map to the plane of a tessellation.
 * <p>
 * Virtual tiles must be realized on a particular {@link TileMap}, and must map to a real tile on
 * its {@link MapLayout}, to become accessible.
 */
public final class VirtualTile<T> {
  public VirtualTile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /** @see TileSelectors#neighbor(int, int, int) */
  public VirtualTile<T> neighbor(int direction) {
    final Coord2 dir = Coord2.directions[direction];
    return new VirtualTile<>(x + dir.x, y + dir.y);
  }

  /** Express the virtual tile as a real tile on the given map. */
  public Tile<T> realize(TileMap<T> map) {
    return map.tiles().at(x, y);
  }

  public final int x;
  public final int y;
}
