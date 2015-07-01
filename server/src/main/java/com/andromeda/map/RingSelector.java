package com.andromeda.map;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class RingSelector<T> implements Tiles<T> {
  public RingSelector(TileMap<T> map, int x, int y, int radius) {
    assert radius >= 0;
    assert map.getLayout().contains(x, y);

    this.map = map;
    originY = x;
    originX = x;
    this.radius = radius;
  }

  @Override
  public Stream<Tile<T>> stream() {
    if (radius > 0)
      return StreamSupport.stream(new RingSpliterator(), false);
    else
      return Stream.of(map.at(originX, originY));
  }

  @Override
  public void forEach(Consumer<? super Tile<T>> action) {
    Spliterator<Tile<T>> spliterator = new RingSpliterator();
    do { } while (spliterator.tryAdvance(action));
  }

  private final TileMap<T> map;
  private final int originX;
  private final int originY;
  private final int radius;


  private final class RingSpliterator extends Spliterators.AbstractSpliterator<Tile<T>> {
    private RingSpliterator() {
      super(Long.MAX_VALUE, 0);

      final Coord2 scaleDir = Direction.coords[Direction.N];
      tile = map.at(originX + radius * scaleDir.x,
                    originY + radius * scaleDir.y);
      direction = 0;
      step = 0;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Tile<T>> action) {
      Objects.requireNonNull(action);

      // Walk along the edge, turn at the corner, and walk until we reach the beginning.
      if (step++ == radius) {
        if (++direction == 6)
          return false;
        // Don't count the same corner twice
        step = 1;
      }

      action.accept(tile);
      tile = tile.getNeighbor(direction);

      return true;
    }

    private int direction;
    private int step;
    private Tile<T> tile;
  }
}
