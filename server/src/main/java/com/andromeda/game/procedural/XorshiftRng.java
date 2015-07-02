package com.andromeda.game.procedural;

final class XorshiftRng extends java.util.Random implements Rng {

  public XorshiftRng(long seed) {
    setSeed(seed);
  }

  @SuppressWarnings("CloneDoesntCallSuperClone")
  @Override
  public Rng clone() {
    return new XorshiftRng(seed);
  }

  @Override
  public synchronized void setSeed(long seed) {
    this.seed = seed;
  }

  @Override
  public int nextInt(int min, int bound) {
    return min + nextInt(bound - min);
  }

  @Override
  public double nextDouble(double bound) {
    return nextDouble() * bound;
  }

  @Override
  public double nextDouble(double min, double bound) {
    return min + nextDouble(bound - min);
  }

  @Override
  public float nextFloat(float bound) {
    return nextFloat() * bound;
  }

  @Override
  public float nextFloat(float min, float bound) {
    return min + nextFloat(bound - min);
  }

  @Override
  protected int next(int nbits) {
    long x = seed;
    x ^= (x << 21);
    x ^= (x >>> 35);
    x ^= (x << 4);
    seed = x;
    x &= ((1L << nbits) - 1);
    return (int) x;
  }

  private long seed;
}
