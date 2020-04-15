package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Set;

/**
 * Defines behavior of a Vertex to be stored in a {@link Graph}.
 *
 * @param <T> Value type stored in Vertex
 * @param <W> Value type stored in {@link Edge}
 */
public interface Vertex<T, W> {

  /**
   * Return value stored in this vertex.
   *
   * @return value stored in this Vertex.
   */
  T getVal();

  /**
   * Return Set of {@link Edge}s connected to this Vertex.
   * @return {@link Set} of {@link Edge}s associated with this Vertex.
   * @throws GraphException Exception thrown while finding {@link Edge}s
   */
  Set<Edge<T, W>> getEdges() throws GraphException;

}
