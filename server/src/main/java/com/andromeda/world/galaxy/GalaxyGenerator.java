package com.andromeda.world.galaxy;

import com.andromeda.game.map.MapGenerator;
import com.andromeda.game.map.Tile;
import com.andromeda.game.map.TileMap;


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
  }

  private static final LayerGen[] layerGen = { new Layer0Gen(), new Layer1Gen() };

  private final TileMap<GalaxyTile> map;
  private final long galaxySeed;
}
