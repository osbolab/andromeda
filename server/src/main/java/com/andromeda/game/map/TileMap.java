package com.andromeda.game.map;

interface TileMap {
  int toKey(int x, int y);

  TileData get(int key);
}
