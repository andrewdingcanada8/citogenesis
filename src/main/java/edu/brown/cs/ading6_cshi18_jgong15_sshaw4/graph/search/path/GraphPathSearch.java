package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.path;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.List;

/**
 * Defines the structure for a path-search on a {@link Graph}.
 * @param <T> Value type stored in {@link Vertex}
 * @param <W> Value type stored in {@link Edge}
 */
public interface GraphPathSearch<T, W> {
  /**
   * Given a starting Vertex and end goal, find a path from the
   * start to end goal (properties of path are up to the implementor).
   * @param start {@link Vertex} to start from
   * @param endVal value stored in target Vertex
   * @return {@link List} of {@link Edge}s that make up path
   * @throws GraphException thrown if there is an error in Graph data
   */
  List<Edge<T, W>> search(Vertex<T, W> start, T endVal) throws GraphException;
}
