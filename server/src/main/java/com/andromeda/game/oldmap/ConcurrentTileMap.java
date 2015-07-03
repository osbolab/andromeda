package com.andromeda.game.oldmap;

import com.andromeda.world.map.Tiles;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Manages a tessellation of {@link Tile} instances with associated objects of type {@code T}.
 * <p>
 * The geometry and topology of the tessellation is defined by the supplied {@link MapLayout}
 * implementation.
 * <p>
 * The orientation of tiles, and of the tessellation, is specified by the directions defined in
 * {@link Coord2}.
 * <p>
 * Thread safety: the data structures in this implementation are either immutable or designed for
 * concurrency.
 */
public final class ConcurrentTileMap<T> implements TileMap<T> {
  public static <T> ConcurrentTileMap<T> allocate(String name, MapLayout layout) {
    return new ConcurrentTileMap<>(name, layout);
  }

  private ConcurrentTileMap(String name, MapLayout layout) {
    this.name = name;
    Objects.requireNonNull(layout);

    this.layout = layout;
    map = new ConcurrentHashMap<>(layout.getMaxTileCount(), 1.0f);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public TileSelectors<T> tiles() {
    return selectors;
  }

  @Override
  public boolean contains(int x, int y) {
    return layout.contains(x, y);
  }

  @Override
  public boolean containsRadius(int x, int y, int radius) {
    return layout.containsRadius(x, y, radius);
  }

  /** Map the {@code (x, y)} point to the underlying storage keyspace. */
  @Override
  public int toKey(int x, int y) {
    return layout.toKey(x, y);
  }

  /** Get the object referenced by the tile with the given key. */
  @Override
  public T get(int key) {
    return map.get(key);
  }

  @Override
  public T set(int key, T contents) {
    return map.put(key, contents);
  }

  @Override
  public T remove(int key) {
    return map.remove(key);
  }


  private final String name;
  private final MapLayout layout;
  private final ConcurrentMap<Integer, T> map;


  /** Greedy and lazy accessors for selecting sets of tiles for reading or writing. */
  private final TileSelectors<T> selectors = new TileSelectors<T>() {
    @Override
    public Tile<T> at(int x, int y) {
      return new Tile<>(ConcurrentTileMap.this, x, y);
    }

    /** Get a reference to the tile one step in {@code direction} from the point {@code (x, y)}.
     *
     * The resulting point must be a real tile in the current {@link MapLayout}.
     *
     * {@param direction} must be an index into {@link Coord2#directions}.
     */
    public Tile<T> neighbor(int x, int y, int direction) {
      final Coord2 dir = Coord2.directions[direction];
      return at(x + dir.x, y + dir.y);
    }

    /** Get a lazily-iterable, greedily-collectable, set of all real tiles in the specified circle.
     *
     * Any points in the circle that do not address real tiles in the current {@link MapLayout}
     * are excluded from the set.
     */
    @Override
    public Tiles<T> inRadius(int x, int y, int radius) {
      return new RadiusSelector<>(ConcurrentTileMap.this, x, y, radius);
    }

    /** Get a lazily-iterable, greedily-collectable, set of all real tiles on the perimeter of the
     * specified circle.
     *
     * Any points on the perimeter that do not address real tiles in the current {@link
     * MapLayout} are excluded from the set.
     */
    @Override
    public Tiles<T> onRing(int x, int y, int radius) {
      return new RingSelector<>(ConcurrentTileMap.this, x, y, radius);
    }
  };
}
