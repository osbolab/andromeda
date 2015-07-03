package com.andromeda.game.oldmap;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Implementations define the geometry of the tessellation of a {@link ConcurrentTileMap}.
 *
 * Thread safety: implementations should expect many concurrent readers of this interface.
 */
@ThreadSafe
public interface MapLayout {
  /**
   * Get the maximum number of tiles that may be tessellated by this instance.
   * <p>
   * The capacity of a layout is necessarily limited to allow consistent mapping between the 2D
   * coordinate space of the layout and the 1D interval of unique indices used to identify tiles.
   */
  int getMaxTileCount();

  /** Return true if the given 2D coordinate addresses a valid tile in the tessellation. */
  boolean contains(int x, int y);

  /** Return {@code true} if all 2D coordinates in the specified circle address valid tiles. */
  boolean containsRadius(int x, int y, int radius);

  /**
   * Project every unique {@code (x, y)} pair to exactly one point on the interval {@code (-j, k)}.
   */
  int toKey(int x, int y);

  /**
   * Project the tile index from the interval {@code (-j, k)} to exactly one {@code (x, y)} pair.
   */
  Coord2 toCoord(int index);
}
