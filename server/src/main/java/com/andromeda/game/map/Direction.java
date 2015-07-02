package com.andromeda.game.map;

public final class Direction {

  public static final int SW = 0;
  public static final int S = 1;
  public static final int SE = 2;
  public static final int NW = 3;
  public static final int N = 4;
  public static final int NE = 5;

  static final Coord2[] coords = new Coord2[]{
      new Coord2(1, 0),
      new Coord2(0, 1),
      new Coord2(-1, 1),
      new Coord2(-1, 0),
      new Coord2(0, -1),
      new Coord2(1, -1)
  };
}
