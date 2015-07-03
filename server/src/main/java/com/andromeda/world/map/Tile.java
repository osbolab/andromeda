package com.andromeda.world.map;

import com.andromeda.procedural.SeedsRng;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;


/** Represents the tile at a given coordinate on a specific map. */
@ThreadSafe
public interface Tile extends SeedsRng {
  int getX();
  int getY();

  @Nullable
  Object get(int layer);
  @Nullable
  Object set(int layer, @Nullable Object newData);

  /** Does not check that the given neighbor is actually on the map. */
  Tile getNeighbor(int direction);
}
