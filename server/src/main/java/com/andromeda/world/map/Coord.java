package com.andromeda.world.map;

import static java.lang.Math.abs;
import static java.lang.Math.max;


public final class Coord {
  public static final int SW = 0;
  public static final int S = 1;
  public static final int SE = 2;
  public static final int NW = 3;
  public static final int N = 4;
  public static final int NE = 5;
  public static final Coord[] directions = new Coord[]{
      new Coord(1, 0),
      new Coord(0, 1),
      new Coord(-1, 1),
      new Coord(-1, 0),
      new Coord(0, -1),
      new Coord(1, -1)
  };

  public static int getDistance(int x1, int y1, int x2, int y2) {
    return max(
        max(abs(x1 - x2), abs(y1 - y2)),
        abs((-x1 - y1) - (-x2 - y2))
    );
  }

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Coord that = (Coord) o;
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
