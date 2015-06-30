package com.andromeda.map;

public class HexagonalMapLayout implements MapLayout {
  public HexagonalMapLayout(int radius) {
    if (radius <= 0)
      throw new IllegalArgumentException("Map radius must be positive");
    if (radius % 2 == 0)
      throw new IllegalArgumentException("Hexagonal map radius must be odd");

    this.radius = radius;
    radiusInv = 1.0 / radius;

    // Iteratively inscribe a triangle with one vertex rooted in the corner,
    // the hypotenuse along the radius, and the adjacent edge growing along the
    // edge of the tessellation.
    // The tessellation's edge length is equal to that of the equilateral triangle.
    int edge = 0;
    int hyp = radius;
    // Integrate the hypotenuse to find the number of tiles in half of the tessellation,
    // including the radius.
    int area = 0;
    while (edge <= hyp) {
      area += hyp--;
      ++edge;
    }
    maxEdgeLength = edge;
    // Reflect over the radius, but don't count it twice.
    maxTileCount = area * 2 - radius;
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
  public int indexOf(int x, int y) {
    return (y * radius) + x;
    // For positive indices only:
    // return (radius + y) * (radius + x + Math.min(0, y));
  }

  @Override
  public AxialCoord positionOf(int index) {
    final int y = (int) Math.round(index * radiusInv);
    final int x = index - y * radius;
    return new AxialCoord(x, y);
  }

  private final int radius;
  private final double radiusInv;
  private final int maxTileCount;
  private final int maxEdgeLength;
}
