package com.andromeda.world.map;

import com.google.common.util.concurrent.ListenableFuture;


public interface TileSelector {
  Tiles now();
  ListenableFuture<Tiles> later();
  TileSelector require(int layer);
}
