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
    return StreamSupport.stream(new RadiusSpliterator(), false);
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

      assert (map.getLayout().contains(originX + row, originY + col));
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
