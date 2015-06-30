package com.andromeda.map;

public class Tile<C> {
  public enum Orientation {
    Flat,
    Pointy
  }

  public Tile(int x, int y, C child) {
    this.x = x;
    this.y = y;
    this.child = child;
  }

  public Tile(int x, int y) {
    this(x, y, null);
  }

  public C get() {
    return child;
  }

  public void set(C child) {
    this.child = child;
  }

  public int computeZ() {
    return -(x + y);
  }

  public final int x;  // col
  public final int y;  // row
  private C child;
}
