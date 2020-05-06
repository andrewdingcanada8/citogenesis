package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.forgetful;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;

/**
 * An implementation of {@link Graph} if there is an external source.
 * @param <T> the value type stored in the Graph's {@link Vertex}
 * @param <W> the value type stored i the Graph's
 * {@link edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge}
 */
public abstract class SourcedForgetfulGraph<T, W> extends SourcedGraph<T, W> {

  /**
   * Given a value of the type stored in the graph's vertices,
   * return a {@link Vertex} stored in that Graph.
   * @param val value to be stored in Vertex
   * @return {@link Vertex} of Vertex that contains val
   * @throws GraphException if {@link SourcedGraph} cannot
   * run {@link SourcedGraph#getEdges(SourcedVertex)}
   */
  @Override
  public Vertex<T, W> getVertex(T val) throws GraphException {
    Vertex<T, W> vert = new SourcedForgetfulVertex<>(val, this);
    return vert;
  }
}
