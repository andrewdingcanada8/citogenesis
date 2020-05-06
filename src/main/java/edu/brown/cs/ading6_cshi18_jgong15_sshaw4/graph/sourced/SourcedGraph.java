package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Set;

/**
 * Implementation of a Graph derived from an external resource.
 * @param <T> type stored in Vertex
 * @param <W> type stored in Edge
 */
public abstract class SourcedGraph<T, W> implements Graph<T, W> {

  /**
   * Given a Vertex, return its corresponding edges.
   * @param val Vertex for lookup
   * @return Edges of val
   * @throws GraphException if error in Source parsing
   */
  public abstract Set<Edge<T, W>> getEdges(SourcedVertex<T, W> val) throws GraphException;

}
