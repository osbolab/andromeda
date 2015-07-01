package com.andromeda.map;

public interface TileSelector<T> {
  Tiles<T> radius(int x, int y, int radius);
  Tiles<T> ring(int x, int y, int radius);
}
