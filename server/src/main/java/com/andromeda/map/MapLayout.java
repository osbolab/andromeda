package com.andromeda.map;

public interface MapLayout {
  int getMaxTileCount();

  int getMaxEdgeLength();

  boolean contains(int x, int y);

  /** Get the index in (-max, max) of the specified axial position. */
  int indexOf(int x, int y);

  /** Get the axial position of the tile with the given index in (-max, max). */
  Coord2 positionOf(int index);
}