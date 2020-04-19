package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;

import java.util.Objects;

public abstract class SourcedVertex<T, W> implements Vertex<T, W> {
  private T val;
  private GraphSource<T, W> source;

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

  public GraphSource<T, W> getSource() {
    return source;
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
    return Objects.equals(val, that.val)
        && Objects.equals(source, that.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val, source);
  }
}
