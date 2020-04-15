package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

/**
 * An implementation of {@link Graph} if there is an external source.
 * @param <T> the value type stored in the Graph's {@link Vertex}
 * @param <W> the value type stored i the Graph's {@link edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge}
 */
public class SourcedGraph<T, W> implements Graph<T, W> {
  private GraphSource<T, W> source;

  /**
   * Constructs a new SourcedGraph.
   * @param source GraphSource to serve as source of Graph
   *               vertices and edges
   */
  public SourcedGraph(GraphSource<T, W> source) {
    this.source = source;
  }

  /**
   * Given a value of the type stored in the graph's vertices,
   * return a {@link Vertex} stored in that Graph.
   * @param val value to be stored in Vertex
   * @return {@link Vertex} of Vertex that contains val
   * @throws GraphException if {@link GraphSource} cannot
   * run {@link GraphSource#getEdges(SourcedVertex)}
   */
  @Override
  public Vertex<T, W> getVertex(T val) throws GraphException {
    Vertex<T, W> vert = new SourcedVertex<>(val, source);
    vert.getEdges();  // test if edges can be obtained to ensure valid source
    return vert;
  }
}
