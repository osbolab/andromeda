package com.andromeda.game.procedural;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public interface Rng {
  Rng clone();

  void setSeed(long seed);

  int nextInt();
  int nextInt(int bound);
  int nextInt(int min, int bound);

  long nextLong();

  double nextDouble();
  double nextDouble(double bound);
  double nextDouble(double min, double bound);

  float nextFloat();
  float nextFloat(float bound);
  float nextFloat(float min, float bound);

  double nextGaussian();

  boolean nextBoolean();

  void nextBytes(byte[] bytes);

  IntStream ints();
  IntStream ints(long streamSize);
  IntStream ints(int min, int bound);
  IntStream ints(long streamSize, int min, int bound);

  LongStream longs();
  LongStream longs(long streamSize);
  LongStream longs(long min, long bound);
  LongStream longs(long streamSize, long min, long bound);

  DoubleStream doubles();
  DoubleStream doubles(long streamSize);
  DoubleStream doubles(double min, double bound);
  DoubleStream doubles(long streamSize, double min, double bound);
}
