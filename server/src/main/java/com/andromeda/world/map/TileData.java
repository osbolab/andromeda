package com.andromeda.world.map;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;


@ThreadSafe
final class TileData {
  synchronized void promoteTo(int layer) {
    assert layer >= 0 : "layer index can't be negative";
    if (layer > maxLayer) maxLayer = layer;
  }

  synchronized Object set(int layer, @Nullable Object newData) {
    promoteTo(layer);

    if (newData == null) return remove(layer);
    if (layers == null) layers = new ArrayList<>(1);

    if (layer == maxLayer) {
      layers.add(new Layer(layer, newData));
      return null;
    }

    for (Layer l : layers) {
      if (l.getIndex() == layer) {
        return set(l, newData);
      }
    }

    layers.add(new Layer(layer, newData));
    return null;
  }

  synchronized Object get(int layer) {
    assert layer >= 0 : "layer index can't be negative";
    assert layer <= maxLayer : "requested data from layer not yet generated for this tile";

    if (layers == null) return null;
    for (final Layer l : layers)
      if (l.getIndex() == layer) return l.getData();
    return null;
  }

  private static Object set(Layer layer, @Nullable Object newData) {
    final Object prev = layer.getData();
    layer.setData(newData);
    return prev;
  }

  private Object remove(int layer) {
    if (layers == null) return null;
    for (final Layer l : layers)
      if (l.getIndex() == layer) return set(l, null);
    return null;
  }

  // The maximum layer to which this tile has been generated.
  private int maxLayer = -1;
  // Behaves as a sparse array
  private List<Layer> layers;
}
