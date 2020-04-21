package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;

import java.util.Objects;

/**
 * Implementation of {@link Edge} to be used for a directed Graph with an external source.
 * This particular edge has every field set on construction. All logic to
 * populate those fields should be encoded
 * in an implementation of {@link SourcedGraph}.
 * @param <T> Value type stored in {@link Vertex}
 * @param <W> Value type stored in {@link SourcedEdge}
 */
public class SourcedEdge<T, W> implements Edge<T, W> {

  private W val;
  private double weight;
  private Vertex<T, W> source;
  private Vertex<T, W> dest;

  /**
   * Creates a new SourcedEdge by setting its fields explicitly.
   * @param val Value to be stored in the edge
   * @param weight weight of the edge
   * @param source source {@link Vertex} of the edge
   * @param dest destination {@link Vertex} of the edge
   */
  public SourcedEdge(W val, double weight, Vertex<T, W> source, Vertex<T, W> dest) {
    this.val = val;
    this.weight = weight;
    this.source = source;
    this.dest = dest;
  }

  /**
   * @return value stored in edge
   */
  @Override
  public W getValue() {
    return val;
  }

  /**
   * @return weight of edge
   */
  @Override
  public double getWeight() {
    return weight;
  }

  /**
   * @return source {@link Vertex} of edge
   */
  @Override
  public Vertex<T, W> getSource() {
    return source;
  }

  /**
   * @return destination {@link Vertex} of edge
   */
  @Override
  public Vertex<T, W> getDest() {
    return dest;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SourcedEdge<?, ?> that = (SourcedEdge<?, ?>) o;
    return Double.compare(that.weight, weight) == 0
        && Objects.equals(val, that.val)
        && Objects.equals(source, that.source)
        && Objects.equals(dest, that.dest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val, weight, source, dest);
  }
}
