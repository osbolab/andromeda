package com.andromeda.game.map;

import java.util.Objects;
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

    final Coord2 scaleDir = Direction.coords[Direction.N];
    startTile = map.at(originX + radius * scaleDir.x,
                       originY + radius * scaleDir.y);
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
    for (Tile<T> tile : toArray())
      action.accept(tile);
  }

  @Override
  public Tile<T>[] toArray() {
    final Tile[] tiles = new Tile[radius * 6];

    Tile<T> tile = startTile;
    int i = 0;
    for (int direction = 0; direction < 6; ++direction) {
      for (int step = 0; step < radius; ++step) {
        tiles[i++] = tile;
        tile = tile.getNeighbor(direction);
      }
    }
    //noinspection unchecked
    return tiles;
  }

  private final TileMap<T> map;
  private final int originX;
  private final int originY;
  private final int radius;
  private final Tile<T> startTile;


  private final class RingSpliterator extends Spliterators.AbstractSpliterator<Tile<T>> {
    private RingSpliterator() {
      super(Long.MAX_VALUE, 0);
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

    private int direction = 0;
    private int step = 0;
    private Tile<T> tile = startTile;
  }
}
