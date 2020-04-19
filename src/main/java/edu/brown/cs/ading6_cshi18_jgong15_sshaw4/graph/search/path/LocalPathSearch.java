package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.path;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

/**
 * Conducts a local lightest-weight path search given some weighting metric,
 * as defined in the {@link Path} objects.
 * @param <T> Value type stored in Graph Vertex
 * @param <W> Value type stored in Graph Edge
 */
class LocalPathSearch<T, W> {
  /**
   * Given a Path object containing the starting Vertex and a goal Vertex value,
   * return a list of edges with the lightest weight starting from the
   * end of the passed path to the target value.
   * @param startPath Path containing starting {@link Vertex}
   * @param endVal Target vertex value
   * @return {@link List} containing edges from lightest-weight path to endVal
   * @throws GraphException if there is an Exception error in Graph data
   */
  protected List<Edge<T, W>> search(Path<T, W> startPath, T endVal) throws GraphException {
    // Initialize a variables to be used in computation
    Path<T, W> curPath = startPath;
    Vertex<T, W> curHead;
    Queue<Path<T, W>> pathQueue = new PriorityQueue<>();
    Set<Vertex<T, W>> visitedVerts = new HashSet<>();
    pathQueue.add(curPath);
    while (!pathQueue.isEmpty()) {
      // acquire current lightest-weight path
      curPath = pathQueue.poll();
      // obtain head from path
      curHead = curPath.getHead();
      if (visitedVerts.contains(curHead)) {
        continue;
      }
      // if head is the same as target, we're done
      if (curHead.getVal().equals(endVal)) {
        return curPath.getPath();
      }
      // if not, say that we visited (and should not visit again)
      visitedVerts.add(curHead);
      // explore head's connected edges, if haven't visited their
      // destination, add them to the queue
      for (Edge<T, W> e : curHead.getEdges()) {
        if (!visitedVerts.contains(e.getDest())) {
          pathQueue.add(curPath.newPathWithEdge(e));
        }
      }
    }
    // Return empty is we exhausted the queue without finding
    // the target value
    return new LinkedList<>();
  }
}
