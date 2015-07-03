package com.andromeda.world.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nullable;


public final class ConcurrentTileCache implements TileDataCache {
  public static ConcurrentTileCache allocate(HexMapLayout layout) {
    return new ConcurrentTileCache(new ConcurrentHashMap<>(layout.getTileCapacity(), 1.0f)
    );
  }

  private ConcurrentTileCache(ConcurrentMap<Integer, TileData> data) {
    this.data = data;
  }

  public ConcurrentTileCache setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  @Nullable
  public TileData get(int key) {
    return data.get(key);
  }

  @Override
  public TileData getOrInit(int key) {
    return data.computeIfAbsent(key, k -> new TileData());
  }

  @Override
  public String toString() {
    if (name != null && !name.isEmpty())
      return "ConcurrentTileCache('" + name + "')";
    return super.toString();
  }

  private String name;
  private final ConcurrentMap<Integer, TileData> data;
}
