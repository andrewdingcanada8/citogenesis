package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;

import java.util.Objects;

public abstract class SourcedVertex<T, W> implements Vertex<T, W> {
  private T val;
  private SourcedGraph<T, W> graph;

  public SourcedVertex(T val, SourcedGraph<T, W> graph) {
    this.val = val;
    this.graph = graph;
  }

  protected SourcedGraph<T, W> getGraph() {
    return graph;
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
