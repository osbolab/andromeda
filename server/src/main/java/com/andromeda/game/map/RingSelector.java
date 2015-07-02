package com.andromeda.game.map;

import java.util.Iterator;
import java.util.NoSuchElementException;


final class RingSelector<T> implements Tiles<T> {

  public RingSelector(TileMap<T> map, int x, int y, int radius) {
    assert radius >= 0;
    assert map.contains(x, y);

    this.map = map;
    originX = x;
    originY = y;
    this.radius = radius;

    final Coord2 scaleDir = Direction.coords[Direction.N];
    startTile = VirtualTile.at(originX + radius * scaleDir.x,
                               originY + radius * scaleDir.y);

    allOnMap = map.containsRadius(startTile.x, startTile.y, radius);
  }

  @Override
  public Iterator<Tile<T>> iterator() {
    return new RingIterator();
  }

  @Override
  public Tile<T>[] toArray() {
    Tile[] tiles = new Tile[radius * 6];

    VirtualTile<T> tile = startTile;
    int i = 0;
    for (int direction = 0; direction < 6; ++direction) {
      for (int step = 0; step < radius; ++step) {
        if (allOnMap || map.contains(tile.x, tile.y))
          tiles[i++] = tile.realize(map);
        tile = tile.neighbor(direction);
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
  private final VirtualTile<T> startTile;
  private final boolean allOnMap;


  private final class RingIterator implements Iterator<Tile<T>> {
    private RingIterator() {
      // Find the first tile on the map
      if (!allOnMap && !map.contains(nextTile.x, nextTile.y))
        moveNext();
    }

    @Override
    public boolean hasNext() {
      return nextTile != null;
    }

    @Override
    public Tile<T> next() {
      if (!hasNext())
        throw new NoSuchElementException();

      final Tile<T> tile = nextTile.realize(map);
      moveNext();
      return tile;
    }

    private void moveNext() {
      do {
        // Walk along the edge, turn at the corner, and walk until we reach the beginning.
        if (step++ == radius) {
          if ((direction = (direction + 1) % 6) == 5) {
            nextTile = null;
            return;
          }
          // Don't count the same corner twice
          step = 1;
        }
        nextTile = nextTile.neighbor(direction);
        // Skip tiles that aren't on the map
      } while (!allOnMap && !map.contains(nextTile.x, nextTile.y));
    }

    private int direction = 0;
    private int step = 0;
    private VirtualTile<T> nextTile = startTile;
  }
}
