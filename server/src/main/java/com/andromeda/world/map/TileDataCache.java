package com.andromeda.world.map;

import javax.annotation.Nullable;


interface TileDataCache {
  @Nullable
  TileData get(int key);
  TileData getOrInit(int key);
}
