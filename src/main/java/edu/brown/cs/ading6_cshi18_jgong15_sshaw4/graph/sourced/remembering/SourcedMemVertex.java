package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.SourceParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.GraphSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;

import java.util.Set;

public class SourcedMemVertex<T, W> extends SourcedVertex<T, W> {
  private Set<Edge<T, W>> edges;
  public SourcedMemVertex(T val, GraphSource<T, W> source) {
    super(val, source);
    edges = null;
  }

  @Override
  public Set<Edge<T, W>> getEdges() throws GraphException {
    if (edges == null) {
      try {
        edges = this.getSource().getEdges(this);
      } catch (SourceParseException e) {
        throw new GraphException(e.getMessage());
      }
    }
    return edges;
  }
}
