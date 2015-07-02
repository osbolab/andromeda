package com.andromeda.world.galaxy;

abstract class Layer {
  Layer(int layer, Object data) {
    assert layer >= 0 && layer <= Byte.MAX_VALUE;
    assert data != null;

    this.layer = (byte) layer;
    this.data = data;
  }

  public Layer next() {
    return next;
  }

  void setNext(Layer next) {
    this.next = next;
  }

  private final byte layer;
  private final Object data;
  private Layer next;
}
