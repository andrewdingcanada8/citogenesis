package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.forgetful;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;

import java.util.Set;

/**
 * An implementation of {@link Vertex} for an external data source.
 *
 * @param <T> Value type stored in {@link Vertex}
 * @param <W> Value type stored in {@link Edge}
 */
public class SourcedForgetfulVertex<T, W> extends SourcedVertex<T, W> {

  /**
   * Constructs a new SourcedVertex.
   *
   * @param val    value to store in Vertex
   * @param source GraphSource to find new edges.
   */
  public SourcedForgetfulVertex(T val, SourcedForgetfulGraph<T, W> source) {
    super(val, source);
  }

  /**
   * @return return {@link Edge} connected to this Vertex.
   * @throws GraphException if a {@link SourcedGraph} throws a {@link GraphException}
   */
  @Override
  public Set<Edge<T, W>> getEdges() throws GraphException {
    return this.getGraph().getEdges(this);
  }
}
