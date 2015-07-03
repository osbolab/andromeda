package com.andromeda.world.map;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public interface Tiles  extends Iterable<Tile> {
  default Stream<Tile> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  Tile[] toArray();
}
