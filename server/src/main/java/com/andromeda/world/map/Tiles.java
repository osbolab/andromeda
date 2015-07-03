package com.andromeda.world.map;

import com.andromeda.world.map.ConcurrentTileMap.TileImpl;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public interface Tiles  extends Iterable<TileImpl> {
  default Stream<TileImpl> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  TileImpl[] toArray();
}
