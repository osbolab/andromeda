package com.andromeda.world.galaxy;


public class Layer1Gen implements LayerGen<GalaxyTile> {
  @Override
  public int getLayer() {
    return 1;
  }

  @Override
  public Layer generate(int x, int y) {
    return null;
  }
}
