package com.andromeda.world.galaxy;

import com.andromeda.game.map.MapGenerator;
import com.andromeda.game.map.Tile;
import com.andromeda.game.map.TileMap;
import com.andromeda.game.map.TileRequest;


final class GalaxyGenerator implements MapGenerator<GalaxyTile> {
  GalaxyGenerator(TileMap<GalaxyTile> map, long galaxySeed) {
    this.map = map;
    this.galaxySeed = galaxySeed;
  }

  @Override
  public Tile<GalaxyTile> toLayer(int x, int y, int layer) {
    assert layer >= 0 && layer < layerGen.length : "no galaxy generator for requested layer";
    Layer newLayer = layerGen[layer].generate(x, y);

    Tile<GalaxyTile> tile = map.tiles().at(x,y);
    if (tile.getMaxLayer() > layer)
      return tile;

    tile.promoteToLayer(layer);
    return tile;
  }

  @Override
  public <R extends TileRequest> void getDependencyTree(R request) {

  }

  private static final LayerGen[] layerGen = { new Gen0() };

  private final TileMap<GalaxyTile> map;
  private final long galaxySeed;
}
