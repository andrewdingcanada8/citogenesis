package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.List;
import java.util.Set;

public interface ComponentSearch<T, W> {

  List<Set<Vertex<T, W>>> search(Vertex<T, W> start) throws GraphException;

}
