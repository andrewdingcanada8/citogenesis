package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.forgetful;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.SourceParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.GraphSource;

import java.util.Objects;
import java.util.Set;

/**
 * An implementation of {@link Vertex} for an external data source.
 *
 * @param <T> Value type stored in {@link Vertex}
 * @param <W> Value type stored in {@link Edge}
 */
public class SourcedForgetfulVertex<T, W> extends edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex<T, W> {

  /**
   * Constructs a new SourcedVertex.
   *
   * @param val    value to store in Vertex
   * @param source GraphSource to find new edges.
   */
  public SourcedForgetfulVertex(T val, GraphSource<T, W> source) {
    super(val, source);
  }

  /**
   * @return return {@link Edge} connected to this Vertex. This calls
   * {@link GraphSource#getEdges(edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex)} as defined in the passed
   * {@link GraphSource}.
   * @throws GraphException if a {@link GraphSource} throws a {@link GraphException}
   */
  @Override
  public Set<Edge<T, W>> getEdges() throws GraphException {
    try {
      return this.getSource().getEdges(this);
    } catch (SourceParseException e) {
      throw new GraphException("Graph error: " + e.getMessage());
    }
  }
}
