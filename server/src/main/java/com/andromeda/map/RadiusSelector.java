package com.andromeda.map;

import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


final class RadiusSelector<T> implements Tiles<T> {
  RadiusSelector(TileMap<T> map, int x, int y, int radius) {
    assert radius >= 0;
    assert map.getLayout().contains(x, y);

    this.map = map;
    originX = x;
    originY = y;
    this.radius = radius;
  }

  @Override
  public Stream<Tile<T>> stream() {
    if (radius > 0)
      return StreamSupport.stream(new RadiusSpliterator(), false);
    else
      return Stream.of(new Tile<T>(map, originX, originY));
  }

  @Override
  public void forEach(Consumer<? super Tile<T>> action) {
    for (Tile<T> tile : toArray())
      action.accept(tile);
  }

  public Tile<T>[] toArray() {
    final Tile[] tiles = new Tile[1 + 3 * radius * radius + 3 * radius];

    int i = 0;
    for (int row = -radius; row <= radius; ++row) {
      for (int col = Math.max(-radius, -row - radius);
           col <= Math.min(radius, -row + radius);
           ++col) {
        tiles[i++] = new Tile<>(map, row, col);
      }
    }

    //noinspection unchecked
    return tiles;
  }

  private final TileMap<T> map;
  private final int originX;
  private final int originY;
  private final int radius;


  private final class RadiusSpliterator extends Spliterators.AbstractSpliterator<Tile<T>> {
    private RadiusSpliterator() {
      super(Long.MAX_VALUE, 0);

      row = -radius;
      row_max = radius;
      resetColumn();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Tile<T>> action) {
      Objects.requireNonNull(action);

      if (col > col_max) {
        if (++row > row_max)
          return false;
        resetColumn();
      }

      action.accept(new Tile<>(map, originX + row, originY + col++));

      return true;
    }

    private void resetColumn() {
      col = Math.max(-radius, -row - radius);
      col_max = Math.min(radius, -row + radius);
    }

    private int row;
    private final int row_max;
    private int col;
    private int col_max;
  }
}
