package com.andromeda.game.map;

public interface TileSelectors<T> {
  Tiles<T> radius(int x, int y, int radius);
  Tiles<T> ring(int x, int y, int radius);
}
