package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public interface GeneratingVertexFinder<T, W> extends VertexFinder<T, W> {
  @Override
  default Vertex<T, W> search(Set<Vertex<T, W>> component) throws GraphException {
    Comparator<Vertex<T, W>> vertCompr = (v1, v2) -> getCompr().compare(v1.getVal(), v2.getVal());
    Vertex<T, W> earliest = Collections.min(component, vertCompr);

    // check connectivity property (all neighbors are a subset of the current component)
    Set<Vertex<T, W>> neighbors = earliest.getEdges().stream()
        .map(e -> e.getDest()).collect(Collectors.toSet());
    if (component.containsAll(neighbors)) {
      return earliest;
    }
    return null;
  }

  /**
   * Obtain comparator for Vertex values.
   *
   * @return comparator for type T
   */
  Comparator<T> getCompr();
}
