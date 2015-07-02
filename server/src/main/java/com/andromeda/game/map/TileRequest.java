package com.andromeda.game.map;

public abstract class TileRequest {
  protected TileRequest(int layer) {
    this.layer = layer;
  }

  @Override
  public String toString() {
    return "layer = " + layer;
  }

  public final int layer;
}
