package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class FunctionCacher<T, W> implements Function<T, W> {

  private LoadingCache<T, W> cache;

  public FunctionCacher(Function<T, W> fun, int size) {
    cache = CacheBuilder.newBuilder()
        .maximumSize(size)
        .build(
            new CacheLoader<>() {
              public W load(T key) throws QueryException {
                return fun.apply(key);
              }
            });
  }

  @Override
  public W apply(T t) {
    try {
      return cache.get(t);
    } catch (ExecutionException e) {
      throw new IllegalArgumentException("bad argument: " + t.toString());
    }
  }
}
