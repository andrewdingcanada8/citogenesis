package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Collections;
import java.util.Set;

public class Deadend<T, W> implements Vertex<T, W> {
  private T deadVal;
  public Deadend(T deadVal) {
    this.deadVal = deadVal;
  }
  @Override
  public T getVal() {
    return deadVal;
  }
  @Override
  public Set<Edge<T, W>> getEdges() throws GraphException {
    return Collections.emptySet();
  }
}
