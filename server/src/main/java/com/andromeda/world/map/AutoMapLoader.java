package com.andromeda.world.map;

public class AutoMapLoader implements TileMap {
  public AutoMapLoader(TileMap map) {
    this.map = map;
  }

  @Override
  public MapLayout getLayout() {
    return map.getLayout();
  }

  @Override
  public TileSelector inRadius(int x, int y, int radius) {
    return null;
  }

  private final TileMap map;
}
