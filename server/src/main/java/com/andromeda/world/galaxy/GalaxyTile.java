package com.andromeda.world.galaxy;

import javax.annotation.Nullable;


public class GalaxyTile {
  public GalaxyTile(int layer) {
    this(layer, null);
  }

  public GalaxyTile(int maxLayer, Layer layer) {
    promote(maxLayer);
    this.layer = layer;
  }

  public void promote(int layer) {
    assert layer >= 0 && layer <= Byte.MAX_VALUE;
    if (layer > maxLayer)
      this.maxLayer = (byte) layer;
  }

  // Maximum layer to which this tile has been generated
  byte maxLayer;
  // Head of a linked list holding the data for each layer, if any.
  @Nullable Layer layer;
}
