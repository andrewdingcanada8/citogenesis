package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;

import java.util.Set;

public class SourcedMemVertex<T, W> extends SourcedVertex<T, W> {
  private Set<Edge<T, W>> edges;

  public SourcedMemVertex(T val, SourcedMemGraph<T, W> graph) {
    super(val, graph);
    edges = null;
  }

  @Override
  public Set<Edge<T, W>> getEdges() throws GraphException {
    if (edges == null) {
      edges = this.getGraph().getEdges(this);
    }
    return edges;
  }
}
