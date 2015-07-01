package com.andromeda.map;

import java.util.stream.Stream;

public interface Tiles<T> {
  Stream<Tile<T>> stream();
}
