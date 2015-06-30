package com.andromeda.map;

public class AxialCoord {
  public AxialCoord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AxialCoord that = (AxialCoord) o;
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
