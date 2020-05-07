package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;

import java.util.Set;

/**
 * Implementation of a Vertex that caches its own edges after loading them.
 * @param <T> type stored in Vertex
 * @param <W> type stored in Edge
 */
public class SourcedMemVertex<T, W> extends SourcedVertex<T, W> {
  private Set<Edge<T, W>> edges;

  /**
   * Constructs a new SourcedMemVertex.
   * @param val value to store
   * @param graph graph instance
   */
  public SourcedMemVertex(T val, SourcedMemGraph<T, W> graph) {
    super(val, graph);
    edges = null;
  }

  /**
   * Return all edges exiting from this node.
   * @return Set of neighboring edges
   * @throws GraphException Exception while obtaining neighboring edges
   */
  @Override
  public Set<Edge<T, W>> getEdges() throws GraphException {
    if (edges == null) {
      edges = this.getGraph().getEdges(this);
    }
    return edges;
  }
}
