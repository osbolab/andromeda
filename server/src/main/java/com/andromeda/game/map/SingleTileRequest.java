package com.andromeda.game.map;

public final class SingleTileRequest extends TileRequest {
  public SingleTileRequest(int x, int y, int layer) {
    super(layer);
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d) at %s", x, y, super.toString());
  }

  public final int x;
  public final int y;
}
