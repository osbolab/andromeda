package com.andromeda.world;

public class Element {

  public static final Element Hydrogen = new Element(
      "Hydrogen", "H", 1.008f, 0.00008988f, 14.01f, 20.28f
  );
  public static final Element Helium = new Element(
      "Helium", "He", 4.002602f, 0.0001785f, 0.95f, 4.22f
  );

  private Element(String name,
                  String symbol,
                  float weight,
                  float density,
                  float meltingK,
                  float boilingK) {
    this.name = name;
    this.symbol = symbol;
    this.weight = weight;
    this.density = density;
    this.meltingK = meltingK;
    this.boilingK = boilingK;
  }

  private final String name;
  private final String symbol;
  private final float weight;
  private final float density;
  private final float meltingK;
  private final float boilingK;
}
