package com.andromeda.world.galaxy;

import com.andromeda.game.map.Tile;
import com.andromeda.game.map.ConcurrentTileMap;
import com.andromeda.game.procedural.Rng;
import com.andromeda.game.procedural.Rngs;
import com.typesafe.config.Config;


/**
 * Generates a galaxy at LoD 0: Black Holes
 */
public final class Layer0 {
  public static Layer0 configure(Config galaxyConf) {
    final Config conf = galaxyConf.getConfig("layer0");
    return new Layer0(
        conf.getDouble("probability"),
        conf.getInt("exlusionRadius")
    );
  }

  private Layer0(double probability, int exclusionRadius) {
    this.probability = probability;
    this.exclusionRadius = exclusionRadius;
  }

  public void generate(long seed, ConcurrentTileMap<GalaxyTile> map, int toRadius) {
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

  private final double probability;
  private final int exclusionRadius;
}
