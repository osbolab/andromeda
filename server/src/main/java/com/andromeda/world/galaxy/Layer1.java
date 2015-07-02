package com.andromeda.world.galaxy;

import com.andromeda.game.map.ConcurrentTileMap;
import com.andromeda.game.procedural.Rng;
import com.andromeda.game.procedural.Rngs;


/**
 * Generate a galaxy at LoD 1: Systems
 */
final class Layer1 {
  void generateInPlace(long seed, ConcurrentTileMap<GalaxyTile> map, int toRadius) {
    final Rng rng = Rngs.fast();
  }
}
