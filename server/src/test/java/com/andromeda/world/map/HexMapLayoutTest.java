package com.andromeda.world.map;

import com.andromeda.game.oldmap.Coord2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class HexMapLayoutTest {
  @Test
  public void computeTileCount() {
    int[] diameters = new int[]{ 1, 3, 5, 7 };
    int[] lengths = new int[]{ 1, 7, 19, 37 };

    for (int i = 0; i < diameters.length; ++i) {
      MapLayout layout = HexMapLayout.withDiameter(diameters[i]);
      assertEquals(lengths[i], layout.getMaxTileCount());
    }
  }

  @Test
  public void computeTileIndex() {
    int[] xs = new int[]{ 0, 1, 2, 3, -1, -2, -3, 1, 2, 3, -1, -2, -3 };
    int[] ys = new int[]{ 0, 0, 0, 0, 0, 0, 0, -1, -2, -3, 1, 2, 3 };
    int[] is = new int[]{ 0, 1, 2, 3, -1, -2, -3, -6, -12, -18, 6, 12, 18 };

    MapLayout layout = HexMapLayout.withDiameter(7);
    for (int i = 0; i < xs.length; ++i)
      assertEquals(is[i], layout.toKey(xs[i], ys[i]));
  }

  @Test
  public void computeTilePosition() {
    int[] xs = new int[]{ 0, 1, 2, 3, -1, -2, -3, 1, 2, 3, -1, -2, -3 };
    int[] ys = new int[]{ 0, 0, 0, 0, 0, 0, 0, -1, -2, -3, 1, 2, 3 };
    int[] is = new int[]{ 0, 1, 2, 3, -1, -2, -3, -6, -12, -18, 6, 12, 18 };

    MapLayout layout = HexMapLayout.withDiameter(7);
    for (int i = 0; i < xs.length; ++i)
      assertEquals(new Coord2(xs[i], ys[i]), layout.toCoord(is[i]));
  }
}
