package com.andromeda.map;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TileMapTest {
  @Test
  public void selectRing() {
    final int[] xs = { 1, 2, 2, 1, 0, 0 };
    final int[] ys = { 0, 0, 1, 2, 2, 1 };

    TileMap<Object> map = new TileMap<>(new HexMapLayout(7));
    List<Tile<Object>> tiles = map.select
        .ring(1, -1, 1)
        .stream()
        .collect(Collectors.toList());

    map.neighbor(0, 0, 1);

    assertEquals(6, tiles.size());

    for (int i = 0; i < tiles.size(); ++i) {
      final Tile<Object> tile = tiles.get(i);
      assertEquals(xs[i], tile.x);
      assertEquals(ys[i], tile.y);
    }

    for (int i = 0; i < 2; ++i) {
      Tile<Object>[] tileArray = map.select.ring(0, 0, i).toArray();
      assertEquals(6*i, tileArray.length);
    }
  }

  @Test
  public void selectRadius() {
    TileMap<Object> map = new TileMap<>(new HexMapLayout(7));
    List<Object> tiles = map.select
        .radius(0, 0, 1)
        .stream()
        .map(Tile::get)
        .collect(Collectors.toList());
    assertEquals(7, tiles.size());

    tiles = map.select
        .radius(0, 0, 2)
        .stream()
        .map(Tile::get)
        .collect(Collectors.toList());
    assertEquals(19, tiles.size());

    tiles = map.select
        .radius(0, 0, 2)
        .stream()
        .filter(Tile::exists)
        .map(Tile::get)
        .collect(Collectors.toList());
    assertEquals(0, tiles.size());

    Tile<Object>[] tileArray = map.select.radius(0, 0, 2).toArray();
    assertEquals(19, tileArray.length);
  }

  @Test
  public void setAndGetSelection() {
    TileMap<Object> map = new TileMap<>(new HexMapLayout(9));

    Set<Object> values = new HashSet<>();

    map.select
        .radius(0, 0, 1)
        .forEach(tile -> {
          Object o = new Object();
          tile.set(o);
          values.add(o);
        });
    assertEquals(7, values.size());

    map.select
        .radius(1, -1, 1)
        .stream()
        .filter(Tile::exists)
        .map(Tile::get)
        .forEach(values::remove);
    assertEquals(3, values.size());

    map.select
        .radius(1, -1, 2)
        .stream()
        .filter(Tile::exists)
        .map(Tile::get)
        .forEach(values::remove);
    assertEquals(0, values.size());
  }

  @Test
  public void setAndGetTiles() {
    TileMap<Object> map = new TileMap<>(new HexMapLayout(9));
    for (int x = -2; x <= 2; ++x) {
      for (int y = -2; y <= 2; ++y) {
        final Object o = new Object();
        map.at(x, y).set(o);

        final Tile<Object> tile = map.at(x, y);
        assertEquals(x, tile.x);
        assertEquals(y, tile.y);
        assertEquals(o, tile.get());
      }
    }
  }
}
