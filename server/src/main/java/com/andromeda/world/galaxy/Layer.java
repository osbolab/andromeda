package com.andromeda.world.galaxy;

abstract class Layer {
  Layer(int layer) {
    assert layer >= 0 && layer <= Byte.MAX_VALUE;
    this.layer = (byte) layer;
  }

  private final byte layer;
  private Layer next;
}
