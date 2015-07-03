package com.andromeda.world.map;


public interface TileMap {
  MapLayout getLayout();

  Tile getTile(int x, int y);
  TileSelector inRadius(int x, int y, int radius);
  //TileSelector onRing(int x, int y, int radius);
  //TileSelector onRing(int x, int y, int r1, int r2);
}
