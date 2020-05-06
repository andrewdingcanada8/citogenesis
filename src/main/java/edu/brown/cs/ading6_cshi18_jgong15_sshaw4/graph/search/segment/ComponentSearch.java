package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.List;
import java.util.Set;

/**
 * Represents algorithm that segments the graph into subcomponents.
 *
 * @param <T> Type stored in Vertex
 * @param <W> Type stored in Edge
 */
public interface ComponentSearch<T, W> {

  /**
   * Returns a list of sets consisting of graph subcomponents.
   *
   * @param start Vertex from which to start segmentation
   * @return List of subcomponents
   * @throws GraphException Error in obtaining neighboring edges and vertices.
   */
  List<Set<Vertex<T, W>>> search(Vertex<T, W> start) throws GraphException;

}
