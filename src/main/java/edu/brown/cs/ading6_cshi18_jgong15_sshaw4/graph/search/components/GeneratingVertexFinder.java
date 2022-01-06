package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Finds the vertex of a set of vertices that minimizes a specified property, whose adjacent
 * vertices are a subset of the original set.
 * @param <T> Type stored in Vertex
 * @param <W> Type stored in Edge
 */
public interface GeneratingVertexFinder<T, W> extends VertexFinder<T, W> {

  /**
   * Returns the minimum vertices whose neighboring vertices are a subset of component.
   * @param component set of vertices
   * @return minimum vertex whose neighboring vertices are a subset of component, null
   * if minimum vertex doesn not satisfy subset property
   * @throws GraphException error in neighboring vertex access
   */
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
