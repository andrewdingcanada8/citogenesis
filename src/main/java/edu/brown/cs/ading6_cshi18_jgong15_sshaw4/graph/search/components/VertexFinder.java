package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Set;

/**
 * Represents an algorithm attempting to find a vertex in a set of vertices.
 * @param <T> Type stored in Vertex
 * @param <W> Type stored in Edge
 */
public interface VertexFinder<T, W> {
  /**
   * Returns vertex satisfying desired property.
   * @param component set of Vertices
   * @return found Vertex
   * @throws GraphException error in Graph movement
   */
  Vertex<T, W> search(Set<Vertex<T, W>> component) throws GraphException;
}
