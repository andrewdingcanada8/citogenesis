package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.database;

import java.util.List;

/**
 * Caching structure for a {@link ListDatabaseProxy}.
 * @param <Q> Query input type
 * @param <R> Query output type (in list)
 */
public class ListDatabaseProxyCacher<Q, R>
    extends DatabaseProxyCacher<Q, List<R>> implements ListDatabaseProxy<Q, R> {
  /**
   * Constructs a new ListDatabaseProxyCacher.
   * @param querier proxy query to cache
   * @param size size of cache (for eviction policy)
   */
  public ListDatabaseProxyCacher(ListDatabaseProxy<Q, R> querier, int size) {
    super(querier, size);
  }
}
