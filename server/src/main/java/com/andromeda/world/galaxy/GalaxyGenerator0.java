package com.andromeda.world.galaxy;

import com.andromeda.game.map.Tile;
import com.andromeda.game.map.TileMap;
import com.andromeda.game.procedural.Rng;
import com.andromeda.game.procedural.Rngs;
import com.andromeda.world.galaxy.GalaxyTile.Type;
import com.typesafe.config.Config;


/**
 * Generates a galaxy at LoD 0: Black Holes
 */
class GalaxyGenerator0 {

  static GalaxyGenerator0 configure(Config conf) {
    return new GalaxyGenerator0(
        conf.getDouble("probability"),
        conf.getInt("exlusionRadius")
    );
  }

  private GalaxyGenerator0(double probability, int exclusionRadius) {
    this.probability = probability;
    this.exclusionRadius = exclusionRadius;
  }

  void generateInPlace(long seed, TileMap<GalaxyTile> map, int toRadius) {
    final Rng rng = Rngs.fast();

    Tile<GalaxyTile> curTile = map.select.at(0, 0);
    // Generate tiles breadth-first
    // Tiles in each radius are dependent on smaller, but not larger, radii.
    for (int radius = 0; radius <= toRadius; ++radius) {
      for (Tile<GalaxyTile> tile : map.select.ring(0, 0, radius).toArray())
        generate(rng, seed, tile);
    }
  }

  private void generate(Rng rng, long seed, Tile<GalaxyTile> tile) {
    double p = probability;

    for (Tile<GalaxyTile> neighbor : tile.neighbors(exclusionRadius).toArray()) {
      if (neighbor.exists() && neighbor.get().type == Type.BlackHole)
        p -= probability * (1.0 / tile.distanceTo(neighbor));
    }

    if (p > 0) {
      rng.setSeed(seed ^ tile.getSeed());
      if (rng.nextDouble() <= p) {
        tile.set(new GalaxyTile(Type.BlackHole));
      }
    }
  }

  private final double probability;
  private final int exclusionRadius;
}
