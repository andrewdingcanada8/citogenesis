package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.GraphPathSearch;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree.DimensionException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree.Locatable;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of A* local search. Objects stored in vertices must implement
 * the {@link Locatable} interface to compute the distance to the specified goal.
 * @param <T> Value type stored in Vertex, must implement Locatable
 * @param <W> Value type stored in Edge
 */
public class AStar<T extends Locatable, W> implements GraphPathSearch<T, W> {
  /**
   * Runs an A* search starting on a specified starting {@link Vertex} and ends
   * on a specified value.
   * @param start starting {@link Vertex}
   * @param endVal target goal value
   * @return {@link List} of {@link Edge} if path exists, or an empty list
   * if path does not exist or given the same start and end goal
   * @throws GraphException if error occurs in obtaining Graph data to continue
   * the search
   */
  @Override
  public List<Edge<T, W>> search(Vertex<T, W> start, T endVal) throws GraphException {
    Path<T, W> path = new AStarPath<>(start, endVal);
    return new LocalPathSearch<T, W>().search(path, endVal);
  }

  /**
   * Reimplementation of a Dijkstra path to incorporate global distance calculation
   * in the path weight. New path weight = total edge weight + global dist from path end to goal
   * @param <T> Value type stored in {@link Vertex} (must implement {@link Locatable}
   * @param <W> Value type stored in edge
   */
  private static class AStarPath<T extends Locatable, W> extends DijkstraPath<T, W> {

    private T end; // end goal value

    /**
     * Constructs a new AStarPath. This is not intended to be used elsewhere in the package
     * (hence private) and will only be used internally for making new paths based
     * on existing ones.
     * @param head new {@link Vertex} at path end.
     * @param end search goal value
     * @param path {@link LinkedList} containing Edges to be in path
     * @param weight starting path weight
     */
    private AStarPath(Vertex<T, W> head, T end, LinkedList<Edge<T, W>> path, double weight) {
      super(head, path, weight);
      this.end = end;
    }

    /**
     * Makes a new AStarPath from scratch.
     * @param head starting {@link Vertex} of the path
     * @param end target search value
     */
    protected AStarPath(Vertex<T, W> head, T end) {
      this(head, end, new LinkedList<>(), 0.0);
    }

    /**
     * Returns weight of the path + distance to global target from path head.
     * @return weight of path + distance to global target from path head
     * @throws GraphException if there is an error in the Graph
     */
    @Override
    public double getTotalWeight() throws GraphException {
      try {
        return super.getTotalWeight() + super.getHead().getVal().distanceTo(end);
      } catch (DimensionException e) {
        throw new GraphException("Graph search exception: " + e.getMessage());
      }
    }

    /**
     * Returns a new path based on the current one, but with a new
     * edge added.
     * @param newEdge new edge to add to the path
     * @return new AStarPath with edge added to path, head set to the end of newEdge,
     * and updated totalWeight
     * @throws GraphException if newEdge is not connected to the end of the current path
     */
    @Override
    public Path<T, W> newPathWithEdge(Edge<T, W> newEdge) throws GraphException {
      LinkedList<Edge<T, W>> newPath = new LinkedList<>(super.getPath());
      // (LinkedList<Edge<T, W>>) super.getPath().clone();
      newPath.add(newEdge);
      return new AStarPath<T, W>(newEdge.getDest(), end, newPath,
          super.getTotalWeight() + newEdge.getWeight());
    }

    @Override
    public String toString() {
      try {
        return "AStarPath{"
            + "distToTarget= " + super.getHead().getVal().distanceTo(end)
            + ", weight=" + super.getTotalWeight()
            + ", path=" + super.getPath()
            + ", end=" + end
            + '}';
      } catch (GraphException | DimensionException e) {
        throw new IllegalStateException("not supposed to be here...");
      }
    }
  }
}
