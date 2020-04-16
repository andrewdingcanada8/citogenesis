package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.path;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import java.util.List;

/**
 * Data structure at the core of a local search lightest-path algorithm.
 * This interface defines the behavior for an object that keeps track of the
 * current path, its weight, and the {@link Vertex} at its head.
 *
 * @param <T> Value type stored in {@link Vertex} of Graph
 * @param <W> Value type stored in {@link Edge} of Graph
 */
public interface Path<T, W> extends Comparable<Path<T, W>> {
  /**
   * Returns total weight of the path (any augmentations
   * to that weight (global distance for A*, for example) should
   * also be represented here.
   *
   * @return weight of path
   * @throws GraphException if there is an Exception in Graph traversal
   */
  double getTotalWeight() throws GraphException;

  /**
   * Return {@link Vertex} at the end of Path.
   * @return {@link Vertex} at the end of Path.
   */
  Vertex<T, W> getHead();

  /**
   * List containing {@link Edge} in Path, ordered from start to finish.
   * @return {@link Edge} in Path (start is the first element of the list)
   */
  List<Edge<T, W>> getPath();

  /**
   * Returns a new Path based on the current one, but extended to include
   * newEdge.
   * @param newEdge {@link Edge} to extend path.
   * @return new Path including newEdge (clone the list containing edges!)
   * @throws GraphException if newEdge is not connected to the current Path.
   */
  Path<T, W> newPathWithEdge(Edge<T, W> newEdge) throws GraphException;

  /**
   * Paths are compared based on their weight (as determined by {@link #getTotalWeight()}.
   * If path1 is less than path2, path1's weight is less than path2's weight.
   * @param oth Path to compare against
   * @return integer (0 if equal, greater than 0 if oth is greater, less than 0 if this is greater)
   */
  @Override
  default int compareTo(Path<T, W> oth) {
    try {
      return Double.compare(this.getTotalWeight(), oth.getTotalWeight());
    } catch (GraphException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
