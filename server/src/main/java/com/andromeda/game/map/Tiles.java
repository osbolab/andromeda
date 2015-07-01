package com.andromeda.game.map;

import java.util.stream.Stream;


public interface Tiles<T>  extends Iterable<Tile<T>> {
  Stream<Tile<T>> stream();

  Tile<T>[] toArray();
}
