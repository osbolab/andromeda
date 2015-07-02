package com.andromeda.game.newmap;

import com.andromeda.game.map.Coord2;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Implementations define the geometry of the tessellation of a tile map.
 * <p>
 * Thread safety: implementations should expect many concurrent readers of this interface.
 */
@ThreadSafe
public interface MapLayout {
  /** Get the unique key for the tile at the specified coordinate. */
  int toKey(int x, int y);

  /**
   * Project the tile key from the interval {@code (-j, k)} to exactly one {@code (x, y)} pair.
   */
  Coord2 toCoord(int key);

  /**
   * Get the maximum number of tiles that may be tessellated in this layout.
   * <p>
   * The capacity is necessarily limited to support mapping tile coordinates to keys.
   */
  int getMaxTileCount();

  /** Return {@code true} if the given coordinate is in this layout. */
  boolean contains(int x, int y);

  /** Return {@code true} if all coordinates in the specified circle are in this layout. */
  boolean containsRadius(int x, int y, int radius);
}
