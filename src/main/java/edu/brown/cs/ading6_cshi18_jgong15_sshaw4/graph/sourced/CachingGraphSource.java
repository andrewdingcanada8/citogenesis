package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.SourceParseException;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 *  A wrapper class to add a layer of Cache to any GraphSource.
 * @param <T> Value type stored in {@link edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex} of graph
 * @param <W> Value type stored in {@link Edge} of graph
 */
public class CachingGraphSource<T, W> implements GraphSource<T, W> {

  private LoadingCache<SourcedVertex<T, W>, Set<Edge<T, W>>> edgeCache; // the cache

  /**
   * Create a CachingGraphSource given any GraphSource to which to add caching functionality.
   * @param source GraphSource to cache
   * @param maxCacheSize size of cache
   */
  public CachingGraphSource(GraphSource<T, W> source, int maxCacheSize) {
    edgeCache = CacheBuilder.newBuilder()
        .maximumSize(maxCacheSize)
        .build(
            new CacheLoader<>() {
              public Set<Edge<T, W>> load(SourcedVertex<T, W> key) throws SourceParseException {
                return source.getEdges(key);
              }
            });
  }

  /**
   * Cached version of getEdges(). Will first check cache if the answer has been computed
   * before, and return the answer if it's stored in the cache, or defer to the given
   * GraphSource if not.
   * @param v {@link edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex} to find {@link Edge}s
   * @return Set of {@link Edge} connected to v
   * @throws SourceParseException if there an Exception with passed GraphSource
   */
  @Override
  public Set<Edge<T, W>> getEdges(SourcedVertex<T, W> v) throws SourceParseException {
    try {
      return edgeCache.get(v);
    } catch (ExecutionException e) {
      throw new SourceParseException("Cache Exception: " + e.getMessage());
    }
  }

  /**
   * Invalidate and clear cache in this instance.
   */
  public void clearCache() {
    edgeCache.invalidateAll();
  }
}
