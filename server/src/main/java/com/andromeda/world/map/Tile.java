package com.andromeda.world.map;

import com.andromeda.procedural.SeedsRng;

import javax.annotation.Nullable;


public interface Tile extends SeedsRng {
  int getX();
  int getY();

  @Nullable
  Object get(int layer);
  @Nullable
  Object set(int layer, @Nullable Object newData);

  @Nullable
  Tile getNeighbor(int direction);
}
