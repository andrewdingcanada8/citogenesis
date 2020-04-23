package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Set;

public interface VertexFinder<T, W> {
  Vertex<T, W> search(Set<Vertex<T, W>> component) throws GraphException;
}
