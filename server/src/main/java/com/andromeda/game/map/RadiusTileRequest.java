package com.andromeda.game.map;

/**
 * A request for the set of tiles, at a particular layer, in a radius around a point.
 */
public final class RadiusTileRequest extends TileRequest {
  public RadiusTileRequest(int x, int y, int radius, int layer) {
    super(layer);
    this.x = x;
    this.y = y;
    this.radius = radius;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d, radius = %d) at %s", x, y, radius, super.toString());
  }

  public final int x;
  public final int y;
  public final int radius;
}
