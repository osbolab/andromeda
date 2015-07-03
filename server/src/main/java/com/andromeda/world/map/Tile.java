package com.andromeda.world.map;


import javax.annotation.concurrent.NotThreadSafe;


/** A proxy to the tile at a given coordinate in a specific tile map. */
@NotThreadSafe
public final class Tile extends VirtualTile {
  Tile(int x, int y, TileMap map) {
    super(x, y);
    this.map = map;
    key = map.toKey(x, y);
  }

  public Object set(int layer, Object newData) {
    if (layers == null) initData();
    return layers.set(layer, newData);
  }

  public Object get(int layer) {
    if (layers == null) initData();
    return layers.get(layer);
  }

  private void initData() {
    layers = map.get(key);
  }

  final int key;
  transient private final TileMap map;
  transient private TileData layers;
}
