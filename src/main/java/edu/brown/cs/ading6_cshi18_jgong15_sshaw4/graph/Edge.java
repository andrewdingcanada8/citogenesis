package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

/**
 * Interface that defines a directed edge in {@link Graph}.
 *
 * @param <T> Value type stored in {@link Vertex}
 * @param <W> Value type stored in {@link Edge}
 */
public interface Edge<T, W> {

  /**
   * Return value stored in Edge.
   *
   * @return value stored in Edge
   */
  W getValue();

  /**
   * Return weight of Edge.
   * @return weight of Edge
   * @throws GraphException error in implementation of Edge
   */
  double getWeight() throws GraphException;

  /**
   * Source vertex of directed edge.
   * @return Source {@link Vertex} of directed edge.
   */
  Vertex<T, W> getSource();

  /**
   * Destination vertex of directed edge.
   * @return Destination {@link Vertex} of directed edge.
   */
  Vertex<T, W> getDest();

}
