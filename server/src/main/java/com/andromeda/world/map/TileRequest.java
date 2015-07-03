package com.andromeda.world.map;

import com.andromeda.world.map.ConcurrentTileMap.TileImpl;

import java.util.Collection;
import java.util.function.Consumer;


public final class TileRequest {
  public TileRequest(int layer, TileSelector selector, Consumer<Collection<TileImpl>> consumer) {
    this.layer = layer;
    this.selector = selector;
    this.consumer = consumer;
  }

  private final int layer;
  private final TileSelector selector;
  private final Consumer<Collection<TileImpl>> consumer;
}
