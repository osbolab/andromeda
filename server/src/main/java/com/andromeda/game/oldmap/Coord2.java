package com.andromeda.game.oldmap;

import static java.lang.Math.abs;
import static java.lang.Math.max;


public final class Coord2 {
  public static final int SW = 0;
  public static final int S = 1;
  public static final int SE = 2;
  public static final int NW = 3;
  public static final int N = 4;
  public static final int NE = 5;
  public static final Coord2[] directions = new Coord2[]{
      new Coord2(1, 0),
      new Coord2(0, 1),
      new Coord2(-1, 1),
      new Coord2(-1, 0),
      new Coord2(0, -1),
      new Coord2(1, -1)
  };

  public static int getDistance(int x1, int y1, int x2, int y2) {
    return max(
        max(abs(x1 - x2), abs(y1 - y2)),
        abs((-x1 - y1) - (-x2 - y2))
    );
  }

  public Coord2(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Coord2 that = (Coord2) o;
    return x == that.x && y == that.y;
  }

  @Override
  public int hashCode() {
    return 31 * y + x;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  public final int x;
  public final int y;
}
