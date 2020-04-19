package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;

public abstract class SourcedGraph<T, W> implements Graph<T, W> {
  private GraphSource<T, W> source;

  public SourcedGraph(GraphSource<T, W> source) {
    this.source = source;
  }

  public GraphSource<T, W> getSource() {
    return source;
  }
}
