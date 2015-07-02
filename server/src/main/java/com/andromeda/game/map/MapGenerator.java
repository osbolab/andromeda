package com.andromeda.game.map;


public interface MapGenerator<T> {
  Tile<T> toLayer(int x, int y, int layer);
  <R extends TileRequest> void getDependencyTree(R request);
}
