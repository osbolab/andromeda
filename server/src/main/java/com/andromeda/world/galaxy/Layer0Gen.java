package com.andromeda.world.galaxy;


public class Layer0Gen implements LayerGen<GalaxyTile> {
  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public Layer generate(int x, int y) {
    return null;
  }
}
