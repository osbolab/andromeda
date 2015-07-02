package com.andromeda.world.galaxy;

public class GalaxyTile {

  public enum Type {
    BlackHole,
    System,
    Nebula,
  }

  GalaxyTile(Type type) {
    this.type = type;
  }

  final Type type;
  private Object child;
}
