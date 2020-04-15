package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.LinkedList;

/**
 * Path implementing weighting metric for a Dijkstra search (only returns
 * total path weight). This is to be used within the Graph.search package
 * ONLY in {@link Dijkstra}.
 * @param <T> Value type stored in Vertex
 * @param <W> Value type stored in Edge
 */
class DijkstraPath<T, W> implements Path<T, W> {
  // explicitly storing head of path and total weight
  // separately so we do not recompute them every time
  private Vertex<T, W> head;              // Vertex at end of path
  private LinkedList<Edge<T, W>> path;    // Edges in ath
  private double totalWeight;             // total path weight

  /**
   * Creates a new DijkstraPath given already-existing values. This
   * is meant for {@link #newPathWithEdge(Edge)}, and should
   * not be used for anything else (or we may have inconsistent
   * paths).
   * @param head Vertex at end of path
   * @param path List of Edges in path
   * @param weight total path weight
   */
  protected DijkstraPath(Vertex<T, W> head, LinkedList<Edge<T, W>> path, double weight) {
    this.head = head;
    this.path = path;
    this.totalWeight = weight;
  }

  /**
   * Creates a new DijkstraPath from scratch.
   * @param head Vertex at start of Path.
   */
  protected DijkstraPath(Vertex<T, W> head) {
    this(head, new LinkedList<>(), 0.0);
  }

  /**
   * Returns total weight of path (sum of weights of edges).
   * @return Total path weight
   * @throws GraphException if there is an error in Graph data
   */
  @Override
  public double getTotalWeight() throws GraphException {
    return totalWeight;
  }

  /**
   * Returns Vertex at end of path.
   * @return head
   */
  @Override
  public Vertex<T, W> getHead() {
    return head;
  }

  /**
   * Edges in path, in order from start to finish.
   * @return {@link LinkedList} containing edges of Path.
   */
  @Override
  public LinkedList<Edge<T, W>> getPath() {
    return path;
  }

  /**
   * Returns a new path containing the current one and extending it to
   * newEdge.
   * @param newEdge new Edge to be included on path
   * @return new Path with updated head, path, and weight with
   * newEdge
   * @throws GraphException if newEdge is not connected to current path
   */
  @Override
  public Path<T, W> newPathWithEdge(Edge<T, W> newEdge) throws GraphException {
    if (!head.equals(newEdge.getSource())) {
      throw new GraphException("NewEdge is not connected to the current path");
    }
    // make sure we don't get our reference lines crossed...
    LinkedList<Edge<T, W>> newPath = new LinkedList<>(path); // (LinkedList) path.clone();
    newPath.add(newEdge);
    return new DijkstraPath<>(newEdge.getDest(), newPath, totalWeight + newEdge.getWeight());
  }

  @Override
  public String toString() {
    return "DijkstraPath{"
        + "path=" + path
        + ", totalWeight=" + totalWeight
        + '}';
  }
}
