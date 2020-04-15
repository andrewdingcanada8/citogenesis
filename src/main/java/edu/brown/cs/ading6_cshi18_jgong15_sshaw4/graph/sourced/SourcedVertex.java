package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.SourceParseException;

import java.util.Objects;
import java.util.Set;

/**
 * An implementation of {@link Vertex} for an external data source.
 *
 * @param <T> Value type stored in {@link Vertex}
 * @param <W> Value type stored in {@link Edge}
 */
public class SourcedVertex<T, W> implements Vertex<T, W> {
  private T val;
  private GraphSource<T, W> source;

  /**
   * Constructs a new SourcedVertex.
   *
   * @param val    value to store in Vertex
   * @param source GraphSource to find new edges.
   */
  public SourcedVertex(T val, GraphSource<T, W> source) {
    this.val = val;
    this.source = source;
  }

  /**
   * Returns value stored in the vertex.
   *
   * @return value stored in the vertex
   */
  @Override
  public T getVal() {
    return val;
  }

  /**
   * @return return {@link Edge} connected to this Vertex. This calls
   * {@link GraphSource#getEdges(SourcedVertex)} as defined in the passed
   * {@link GraphSource}.
   * @throws GraphException if a {@link GraphSource} throws a {@link GraphException}
   */
  @Override
  public Set<Edge<T, W>> getEdges() throws GraphException {
    try {
      return source.getEdges(this);
    } catch (SourceParseException e) {
      throw new GraphException("Graph error: " + e.getMessage());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SourcedVertex<?, ?> that = (SourcedVertex<?, ?>) o;
    return Objects.equals(val, that.val);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }
}
