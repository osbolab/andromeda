package com.andromeda.game.map;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Tiles<T> {
  Stream<Tile<T>> stream();

  void forEach(Consumer<? super Tile<T>> action);

  Tile<T>[] toArray();
}
