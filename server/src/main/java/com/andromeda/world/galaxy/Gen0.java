package com.andromeda.world.galaxy;

import com.andromeda.game.map.Tile;
import com.andromeda.game.map.TileMap;
import com.andromeda.game.procedural.Rng;
import com.andromeda.game.procedural.Rngs;
import com.typesafe.config.Config;


/**
 * Generates a galaxy at LoD 0: Black Holes
 */
public final class Gen0 implements LayerGen {
  public static Gen0 configure(Config galaxyConf) {
    final Config conf = galaxyConf.getConfig("layer0");
    return new Gen0(
        conf.getDouble("probability"),
        conf.getInt("exlusionRadius")
    );
  }

  Gen0() {
    probability = 0.0;
    exclusionRadius = 2;
  }

  private Gen0(double probability, int exclusionRadius) {
    this.probability = probability;
    this.exclusionRadius = exclusionRadius;
  }

  public void generate(long seed, TileMap<GalaxyTile> map, int toRadius) {
    final Rng rng = Rngs.fast();
    // Generate tiles breadth-first
    // Tiles in each radius are dependent on smaller, but not larger, radii.
    for (int radius = 0; radius <= toRadius; ++radius) {
      for (Tile<GalaxyTile> tile : map.tiles().onRing(0, 0, radius))
        generate(rng, seed, tile);
    }
  }

  private void generate(Rng rng, long seed, Tile<GalaxyTile> tile) {
    double p = probability;

    for (Tile<GalaxyTile> neighbor : tile.neighbors(exclusionRadius)) {
      if (neighbor.exists() && neighbor.get().maxLayer == 0)
        p -= probability * (1.0 / tile.distanceTo(neighbor));
    }

    if (p > 0) {
      rng.setSeed(seed ^ tile.getSeed());
      if (rng.nextDouble() <= p) {
        tile.set(new GalaxyTile(0, null));
      }
    }
  }

  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public Layer generate(int x, int y) {
    return null;
  }

  private final double probability;
  private final int exclusionRadius;
}
