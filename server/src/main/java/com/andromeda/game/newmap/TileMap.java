package com.andromeda.game.newmap;

interface TileMap {
  int toKey(int x, int y);

  TileData get(int key);
}
