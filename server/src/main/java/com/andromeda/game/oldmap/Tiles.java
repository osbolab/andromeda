package com.andromeda.game.oldmap;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public interface Tiles<T>  extends Iterable<Tile<T>> {
  default Stream<Tile<T>> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  Tile<T>[] toArray();
}
