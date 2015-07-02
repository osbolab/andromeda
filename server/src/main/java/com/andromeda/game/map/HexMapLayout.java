package com.andromeda.game.map;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.round;


public final class HexMapLayout implements MapLayout {
  public static HexMapLayout withDiameter(int diameter) {
    return new HexMapLayout(diameter);
  }

  private HexMapLayout(int diameter) {
    assert diameter > 0 : "Map diameter must be positive";
    assert (diameter & 1) != 0 : "Hexagonal map diameter must be odd";

    this.diameter = diameter;
    // Cache for frequent divisions
    diameterInv = 1.0f / diameter;
    radius = diameter / 2;
    // The edge length is equal to the radius including the center tile.
    maxEdgeLength = 1 + radius;
    // Sum the perimeters of the hexes with radii 1..R and add the center tile.
    maxTileCount = 1 + 3 * radius * radius + 3 * radius;
  }

  @Override
  public int getMaxTileCount() {
    return maxTileCount;
  }

  @Override
  public int getMaxEdgeLength() {
    return maxEdgeLength;
  }

  @Override
  public boolean contains(int x, int y) {
    return max(abs(x), abs(y)) <= radius
           && abs(-x - y) <= radius;
  }

  @Override
  public boolean containsRadius(int x, int y, int radius) {
    assert radius >= 0 : "Radius can't be negative";
    return (radius = this.radius - radius) >= 0
           && max(abs(x), abs(y)) <= radius
           && abs(-x - y) <= radius;
  }

  @Override
  public int indexOf(int x, int y) {
    assert contains(x, y) : "Tile position is not in map";
    return (y * diameter) + x;
    // For positive indices only:
    // return (diameter + y) * (diameter + x + Math.min(0, y));
  }

  @Override
  public Coord2 positionOf(int index) {
    final int y = round(index * diameterInv);
    final int x = index - y * diameter;
    assert contains(x, y) : "Position of tile for index is not in map";
    return new Coord2(x, y);
  }

  private final int radius;
  private final int diameter;
  private final float diameterInv;
  private final int maxTileCount;
  private final int maxEdgeLength;
}
