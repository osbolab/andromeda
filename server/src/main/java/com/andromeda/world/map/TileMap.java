package com.andromeda.world.map;


public interface TileMap {
  int toKey(int x, int y);

  TileData get(int key);

  default TileSelectors select(int layer) {
    return null;
  }
}
