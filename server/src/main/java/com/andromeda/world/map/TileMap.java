package com.andromeda.world.map;


public interface TileMap {
  MapLayout getLayout();

  TileSelector inRadius(int x, int y, int radius);
  //TileSelector onRing(int x, int y, int radius);
  //TileSelector onRing(int x, int y, int r1, int r2);
}
