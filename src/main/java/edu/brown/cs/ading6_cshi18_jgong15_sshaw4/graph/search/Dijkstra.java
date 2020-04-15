package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.GraphPathSearch;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.List;

/**
 * Implementation of Dijkstra's lightest weight path search.
 * @param <T> Value type stored in {@link Vertex}
 * @param <W> Value type stored in {@link Edge}
 */
public class Dijkstra<T, W> implements GraphPathSearch<T, W> {
  /**
   * Conducts a Dijkstra search given a starting Vertex and
   * target goal value.
   * @param start {@link Vertex} to start from
   * @param endVal Target value
   * @return {@link List} containing Edges from start to endVal. Will return
   * an empty list of there is no path, or if start contains endVal.
   * @throws GraphException if there is an Exception in Graph data
   */
  @Override
  public List<Edge<T, W>> search(Vertex<T, W> start, T endVal) throws GraphException {
    Path<T, W> path = new DijkstraPath<>(start);
    return new LocalPathSearch<T, W>().search(path, endVal);
  }

}
