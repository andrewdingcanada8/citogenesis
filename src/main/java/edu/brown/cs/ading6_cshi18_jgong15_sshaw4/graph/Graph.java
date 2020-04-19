package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

/**
 * Define the behavior of a directed Graph.
 * @param <T> Value type stored in {@link Vertex}
 * @param <W> Value type stored in {@link Edge}
 */
public interface Graph<T, W> {
  /**
   * Given a vertex value, find the corresponding {@link Vertex} in the graph.
   * @param val value stored in a {@link Vertex} in graph
   * @return corresponding {@link Vertex} in graph
   * @throws GraphException if val is not found in a {@link Vertex} in Graph
   */
  Vertex<T, W> getVertex(T val) throws GraphException;
}
