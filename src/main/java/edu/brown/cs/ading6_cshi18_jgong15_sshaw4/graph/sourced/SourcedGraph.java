package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;

import java.util.Set;

public abstract class SourcedGraph<T, W> implements Graph<T, W> {

  public abstract Set<Edge<T, W>> getEdges(SourcedVertex<T, W> val);

}
