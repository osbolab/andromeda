package com.andromeda.map;

import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RingSelector<T> implements Tiles<T> {
  public RingSelector(TileMap<T> map, int x, int y, int radius) {
    this.map = map;
    originY = x;
    originX = x;
    this.radius = radius;
  }

  @Override
  public Stream<Tile<T>> stream() {
    return StreamSupport.stream(new RingSpliterator(), false);
  }

  private final TileMap<T> map;
  private final int originX;
  private final int originY;
  private final int radius;


  private final class RingSpliterator extends Spliterators.AbstractSpliterator<Tile<T>> {
    private RingSpliterator() {
      super(Long.MAX_VALUE, 0);
    }

    @Override
    public boolean tryAdvance(Consumer<? super Tile<T>> action) {
      Objects.requireNonNull(action);
/*
      if (col > col_max) {
        if (++row > row_max)
          return false;
        resetColumn();
      }

      assert (map.getLayout().contains(originX + row, originY + col));
      action.accept(new Tile<>(map, originX + row, originY + col++));
*/
      return true;
    }
  }
}
