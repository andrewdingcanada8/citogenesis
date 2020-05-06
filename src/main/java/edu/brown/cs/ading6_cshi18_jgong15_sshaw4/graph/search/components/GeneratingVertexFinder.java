package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
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
    Set<Vertex<T, W>> validVerts = component.stream()
        .filter(v -> { // if the object is validatable, return true if it's valid
          if (v.getVal() instanceof Validatable) {
            return ((Validatable) v.getVal()).isValid();
          } // all objects that do not implement validatable interface are valid
          return true;
        })
        .collect(Collectors.toSet());
    if (validVerts.isEmpty()) {
      return null;
    }
    Vertex<T, W> earliest = Collections.min(validVerts, vertCompr);

    // check connectivity property (all neighbors are a subset of the current component)
    Set<Vertex<T, W>> neighbors = earliest.getEdges().stream()
        .map(Edge::getDest)
        .filter(v -> { // if the object is validatable, return true if it's valid
          if (v.getVal() instanceof Validatable) {
            return ((Validatable) v.getVal()).isValid();
          } // all objects that do not implement validatable interface are valid
          return true;
        })
        .collect(Collectors.toSet());
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
