package com.andromeda.game.map;

public interface TileMap<T> {
  String getName();

  int toKey(int x, int y);

  TileSelectors<T> tiles();

  boolean contains(int x, int y);
  boolean containsRadius(int x, int y, int radius);

  T get(int key);
  T set(int key, T contents);
  T remove(int key);
}
