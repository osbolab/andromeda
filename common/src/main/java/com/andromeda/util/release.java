package com.andromeda.util;

import java.util.function.Consumer;
import java.util.function.Function;

import io.netty.util.ReferenceCounted;

public final class release {
  public static void now(ReferenceCounted refCounted) {
    refCounted.release();
  }

  public static <T extends ReferenceCounted> void after(T refCounted, Consumer<T> doing) {
    try {
      doing.accept(refCounted);
    } finally {
      refCounted.release();
    }
  }

  public static <T extends ReferenceCounted, A> A returning(T refCounted,
                                                            Function<T, A> doing) {
    try {
      return doing.apply(refCounted);
    } finally {
      refCounted.release();
    }
  }

  public static <T extends ReferenceCounted> auto<T> auto(Object refCounted) {
    //noinspection unchecked
    return new auto<>((T) refCounted);
  }

  public static class auto<T extends ReferenceCounted> implements AutoCloseable {
    private auto(T refCounted) {
      t = refCounted;
    }

    public T get() {
      return t;
    }

    public synchronized void release() {
      if (t != null) {
        t.release();
        t = null;
      }
    }

    @Override
    public synchronized void close() throws Exception {
      release();
    }

    private T t;
  }
}
