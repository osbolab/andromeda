package com.andromeda.server;

import com.andromeda.procedural.Rng;
import com.andromeda.procedural.Rngs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  public static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    Rng rng = Rngs.fast();
    rng.longs(10)
       .forEach(i -> System.out.print(i + ", "));

    Rng rng2 = rng.clone();

    System.out.println();
    System.out.println("Cloned:");
    rng.doubles(10).forEach(i -> System.out.print(i + ", "));
    System.out.println();
    rng2.doubles(10).forEach(i -> System.out.print(i + ", "));
  }
}
