package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.database;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * Stores a finite number of the results of past queries in a cache for faster future access to
 * those results.
 * @param <Q> the type of the query, which is used as the type of the key in the proxy
 * @param <R> the type of the queried result
 */
public class DatabaseProxyCacher<Q, R> implements DatabaseProxy<Q, R> {
  private LoadingCache<Q, R> cache;

  /**
   * Constructs a DatabaseProxyCacher object, which caches the query calls to the input querier.
   * @param querier the Object whose calls to query the cache stores
   * @param size the maximum number of calls stored in the cache to be constructed
   */
  public DatabaseProxyCacher(DatabaseProxy<Q, R> querier, int size) {
    cache = CacheBuilder.newBuilder()
            .maximumSize(size)
            .build(
                  new CacheLoader<Q, R>() {
                    public R load(Q key) throws DatabaseProxyException {
                      return querier.query(key);
                    }
                  });
  }

  /**
   * Queries the cache of the current DatabaseProxy object for an object of type Q.
   * @param input represents the query to be made
   * @return A List of objects of type R, corresponding to the type of the results
   * @throws DatabaseProxyException if the database could not be queried
   */
  @Override
  public R query(Q input) throws DatabaseProxyException {
    try {
      return cache.get(input);
    } catch (ExecutionException e) {
      throw new DatabaseProxyException("There was an error in a cache: " + e.getCause());
    }
  }
}
