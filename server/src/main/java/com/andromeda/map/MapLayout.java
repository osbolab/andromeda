package com.andromeda.map;

public interface MapLayout {
  int getMaxTileCount();

  int getMaxEdgeLength();

  /**
   * Get the index in (-max, max) of the specified tile position.
   */
  int indexOf(int x, int y);

  /**
   * Get the (x, y) position of the tile with the given index in (-max, max).
   */
  AxialCoord positionOf(int index);
}
