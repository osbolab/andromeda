package com.andromeda.world.system;

import com.andromeda.world.Element;

import org.apache.commons.math3.util.Precision;


/**
 * A Star is a sphere of plasma held together by its own gravity.
 * <p>
 * Stars begin life as gaseous nebulae containing stellar Hydrogen. At sufficient density Hydrogen
 * in the star's core will fuse into Helium.
 * <p>
 * When the core Hydrogen is exhausted the star will expand, sometimes fusing heavier elements
 * before degenerating. A degenerate star releases its products, seeding the formation of new stars
 * with heavier core compositions.
 */
public class Star {

  private Star(int age, int mass, Element[] composition, float[] compositionProps) {
    assert composition.length == compositionProps.length;
    assert sumsToOne(compositionProps) : "Star composition proportions must sum to 1";

    this.age = age;
    this.mass = mass;
    this.composition = composition;
    this.compositionProps = compositionProps;
  }

  private static boolean sumsToOne(float[] fs) {
    double s = 0.0;
    for (double f : fs)
      s += f;
    return Precision.equals(s, 1.0, 4);
  }

  private final int age;  // Galactic Years
  private final int mass; // Solar Mass Units
  private final Element[] composition;
  private final float[] compositionProps;
}
