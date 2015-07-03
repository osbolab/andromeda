package com.andromeda.procedural;

public final class Rngs {

  public static Rng fast(long seed) {
    return new XorshiftRng(seed);
  }

  public static Rng fast() {
    return fast(System.nanoTime());
  }
}
