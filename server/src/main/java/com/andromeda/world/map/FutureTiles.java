package com.andromeda.world.map;

import com.google.common.util.concurrent.ListenableFuture;


public interface FutureTiles extends ListenableFuture<Tiles>, Iterable<Tile> {
}
