package com.andromeda.game.map;

public interface TileSelectors<T> {
  Tile<T> at(int x, int y);
  Tile<T> neighbor(int x, int y, int direction);

  Tiles<T> inRadius(int x, int y, int radius);
  Tiles<T> onRing(int x, int y, int radius);
}
