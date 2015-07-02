package com.andromeda.world.galaxy;

interface LayerGen {
  int getLayer();
  Layer generate(int x, int y);
}
