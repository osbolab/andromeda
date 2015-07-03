package com.andromeda.game.map;

import com.andromeda.game.oldmap.Coord2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.round;


/**
 * Implements the math, e.g. 2D position to 1D index, for a hexagonal tessellation of hexagonal
 * tiles. Does not specify the orientation of tiles or tessellation (i.e. pointy- or flat-topped).
 * <p>
 * Thread safety: instances of this class have no mutable state.
 */
@ThreadSafe
public final class HexMapLayout implements MapLayout {
  public static HexMapLayout withDiameter(int diameter) {
    return new HexMapLayout(diameter);
  }

  private HexMapLayout(int diameter) {
    assert diameter > 0 : "map diameter must be positive";
    assert (diameter & 1) != 0 : "hexagonal map diameter must be odd";

    this.diameter = diameter;
    // Cache for frequent divisions
    diameterInv = 1.0f / diameter;
    radius = diameter / 2;
    // Sum the perimeters of the hexes with radii 1..R and add the center tile.
    maxTileCount = 1 + 3 * radius * radius + 3 * radius;

    if (log.isDebugEnabled())
      log.debug("%s(diameter = %d, maxTileCount = %d)",
                getClass().getSimpleName(),
                diameter,
                maxTileCount);
  }

  @Override
  public int toKey(int x, int y) {
    assert contains(x, y) : "coordinate does not map to layout";
    return (y * diameter) + x;
    // For positive indices only:
    // return (diameter + y) * (diameter + x + Math.min(0, y));
  }

  @Override
  public Coord2 toCoord(int key) {
    final int y = round(key * diameterInv);
    final int x = key - y * diameter;
    assert contains(x, y) : "key does not map to layout";
    return new Coord2(x, y);
  }

  @Override
  public int getMaxTileCount() {
    return maxTileCount;
  }

  @Override
  public boolean contains(int x, int y) {
    return max(abs(x), abs(y)) <= radius
           && abs(-x - y) <= radius;
  }

  @Override
  public boolean containsRadius(int x, int y, int radius) {
    assert radius >= 0 : "radius can't be negative";
    return (radius = this.radius - radius) >= 0
           && max(abs(x), abs(y)) <= radius
           && abs(-x - y) <= radius;
  }

  private final int radius;
  private final int diameter;
  private final float diameterInv;
  private final int maxTileCount;

  private static final Logger log = LoggerFactory.getLogger(HexMapLayout.class);
}
