package com.andromeda.game.map;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


public class ConcurrentTileMapTest {

  @Test
  public void selectRingAsStream() {
    final int[] xs = { 1, 2, 2, 1, 0, 0 };
    final int[] ys = { -2, -2, -1, 0, 0, -1 };

    ConcurrentTileMap<Object> map = ConcurrentTileMap.allocate(HexMapLayout.withDiameter(7));
    List<Tile<Object>> tiles = map.tiles()
        .onRing(1, -1, 1)
        .stream()
        .collect(Collectors.toList());

    assertEquals(6, tiles.size());

    for (int i = 0; i < tiles.size(); ++i) {
      final Tile<Object> tile = tiles.get(i);
      assertEquals(xs[i], tile.x);
      assertEquals(ys[i], tile.y);
    }
  }

  @Test
  public void selectRingAsArray() {
    ConcurrentTileMap<Object> map = ConcurrentTileMap.allocate(HexMapLayout.withDiameter(7));
    for (int i = 0; i < 2; ++i) {
      Tile<Object>[] tileArray = map.tiles().onRing(0, 0, i).toArray();
      assertEquals(6 * i, tileArray.length);
    }
  }

  @Test
  public void selectOffMapRing() {
    final int[] xs = { 2, 1, 0, 0 };
    final int[] ys = { -2, -1, -1, -2 };

    ConcurrentTileMap<Object> map = ConcurrentTileMap.allocate(HexMapLayout.withDiameter(5));
    List<Tile<Object>> tiles = map.tiles()
        .onRing(1, -2, 1)
        .stream()
        .collect(Collectors.toList());

    assertEquals(4, tiles.size());

    for (int i = 0; i < tiles.size(); ++i) {
      final Tile<Object> tile = tiles.get(i);
      assertEquals(xs[i], tile.x);
      assertEquals(ys[i], tile.y);
    }
  }

  @Test
  public void selectRadius() {
    ConcurrentTileMap<Object> map = ConcurrentTileMap.allocate(HexMapLayout.withDiameter(7));
    List<Object> tiles = map.tiles()
        .inRadius(0, 0, 1)
        .stream()
        .map(Tile::get)
        .collect(Collectors.toList());
    assertEquals(7, tiles.size());

    tiles = map.tiles()
        .inRadius(0, 0, 2)
        .stream()
        .map(Tile::get)
        .collect(Collectors.toList());
    assertEquals(19, tiles.size());

    tiles = map.tiles()
        .inRadius(0, 0, 2)
        .stream()
        .filter(Tile::exists)
        .map(Tile::get)
        .collect(Collectors.toList());
    assertEquals(0, tiles.size());

    Tile<Object>[] tileArray = map.tiles().inRadius(0, 0, 2).toArray();
    assertEquals(19, tileArray.length);
  }

  @Test
  public void setAndGetSelection() {
    ConcurrentTileMap<Object> map = ConcurrentTileMap.allocate(HexMapLayout.withDiameter(9));

    Set<Object> values = new HashSet<>();

    map.tiles()
        .inRadius(0, 0, 1)
        .stream()
        .forEach(tile -> {
          Object o = new Object();
          tile.set(o);
          values.add(o);
        });
    assertEquals(7, values.size());

    map.tiles()
        .inRadius(1, -1, 1)
        .stream()
        .filter(Tile::exists)
        .map(Tile::get)
        .forEach(values::remove);
    assertEquals(3, values.size());

    map.tiles()
        .inRadius(1, -1, 2)
        .stream()
        .filter(Tile::exists)
        .map(Tile::get)
        .forEach(values::remove);
    assertEquals(0, values.size());
  }

  @Test
  public void setAndGetTiles() {
    ConcurrentTileMap<Object> map = ConcurrentTileMap.allocate(HexMapLayout.withDiameter(9));
    for (int x = -2; x <= 2; ++x) {
      for (int y = -2; y <= 2; ++y) {
        final Object o = new Object();
        map.tiles().at(x, y).set(o);

        final Tile<Object> tile = map.tiles().at(x, y);
        assertEquals(x, tile.x);
        assertEquals(y, tile.y);
        assertEquals(o, tile.get());
      }
    }
  }
}
