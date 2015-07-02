package com.andromeda.world.galaxy;

import com.andromeda.game.map.Coord2;
import com.andromeda.game.map.RadiusTileRequest;


final class NewGen0 implements LayerGen {
  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public Layer generate(int x, int y) {
    return null;
  }

  /** Flood fill the map from the origin to encompass the requested radius. */
  RadiusTileRequest getDependencies(RadiusTileRequest req) {
    if (req.x != 0 || req.y != 0) {
      final int radius = Coord2.getDistance(0, 0, req.x, req.y) + req.radius;
      return new RadiusTileRequest(0, 0, radius, 0);
    }
return null;
    // Requests for a radius from the origin are generated directly
  }
}
