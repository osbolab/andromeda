package com.andromeda.game.oldmap;

import com.andromeda.world.map.Tiles;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.max;
import static java.lang.Math.min;


final class RadiusSelector<T> implements Tiles<T> {
  RadiusSelector(ConcurrentTileMap<T> map, int x, int y, int radius) {
    assert radius >= 0;
    assert map.contains(x, y);

    this.map = map;
    allOnMap = map.containsRadius(x, y, radius);
    originX = x;
    originY = y;
    this.radius = radius;
  }

  @Override
  public Iterator<Tile<T>> iterator() {
    return new RadiusIterator();
  }

  public Tile<T>[] toArray() {
    Tile[] tiles = new Tile[1 + 3 * radius * radius + 3 * radius];

    int i = 0;
    for (int row = -radius; row <= radius; ++row) {
      for (int col = max(-radius, -row - radius);
           col <= min(radius, -row + radius);
           ++col) {
        if (allOnMap || map.contains(row, col))
          tiles[i++] = new Tile<>(map, row, col);
      }
    }

    if (i < tiles.length) {
      // Some tiles were off the map
      Tile[] newTiles = new Tile[i];
      System.arraycopy(tiles, 0, newTiles, 0, newTiles.length);
      tiles = newTiles;
    }

    //noinspection unchecked
    return tiles;
  }

  private final TileMap<T> map;
  private final int originX;
  private final int originY;
  private final int radius;
  private final boolean allOnMap;


  private final class RadiusIterator implements Iterator<Tile<T>> {
    private RadiusIterator() {
      row = -radius;
      row_max = radius;
      resetColumn();
    }

    @Override
    public boolean hasNext() {
      return row < row_max || col <= col_max;
    }

    @Override
    public Tile<T> next() {
      if (col > col_max) {
        if (++row > row_max)
          throw new NoSuchElementException();
        resetColumn();
      }
      final int x = originX + row;
      final int y = originY + col++;

      if (!allOnMap && !map.contains(x, y))
        return next();

      return new Tile<>(map, x, y);
    }

    private void resetColumn() {
      col = max(-radius, -row - radius);
      col_max = min(radius, -row + radius);
    }

    private int row;
    private final int row_max;
    private int col;
    private int col_max;
  }
}
