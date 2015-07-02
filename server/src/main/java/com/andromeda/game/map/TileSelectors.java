package com.andromeda.game.map;

public interface TileSelectors<T> {
  Tile<T> at(int x, int y);
  Tile<T> neighbor(int x, int y, int direction);

  Tiles<T> radius(int x, int y, int radius);
  Tiles<T> ring(int x, int y, int radius);
}
