package com.andromeda.world.map;

import javax.annotation.concurrent.NotThreadSafe;


/**
 * Layers create a dependence hierarchy between the data attached to a tile in the game world.
 * Specifically, objects in a higher layer depend on information from objects in the lower layer
 * <p>
 * For example, a system in a galaxy depends on the lack of a black hole in the same tile. The
 * system would therefore be defined in Layer 1 and would depend on
 * <p>
 * <ol>
 * <li>layer 0 having been generated to determine the presence of a black hole</li>
 * <li>the absence of black hole data in Layer 0</li>
 * </ol>
 * <p>
 * The {@code Layer} class is thus used by the galaxy generator to
 * <p>
 * <ol>
 * <li>identify which tiles need to have black holes generated</li>
 * <li>identify which tiles contain black holes</li>
 * <li>attach solar system data to suitable tiles</li>
 * </ol>
 * </p>
 */
@NotThreadSafe
public final class Layer {
  Layer(int layer) {
    this(layer, null);
  }

  Layer(int layer, Object data) {
    assert layer >= 0 : "layer index can't be negative";
    this.index = layer;
    this.data = data;
  }

  public int getIndex() {
    return index;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  private final int index;
  private Object data;
}
