package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;


public abstract class RootedSourcedMemGraph<T, W> extends SourcedMemGraph<T, W> {
  // rather than extension, this behavior may be integrated by composition
  // I'll stick with this now, but will get back to it if there's time

  private static final int MAX_DEPTH_DEFAULT = 1000;
  private T headVal;
  private Vertex<T, W> head;
  private Map<T, Integer> depthMap;
  private int maxDepth;

  public RootedSourcedMemGraph(T headVal) throws GraphException {
    this(headVal, MAX_DEPTH_DEFAULT);
  }

  public RootedSourcedMemGraph(T headVal, int maxDepth) throws GraphException {
    this.headVal = headVal;
    this.maxDepth = maxDepth;
    depthMap = new HashMap<>();
    loadByBFS();
  }

  public Vertex<T, W> getHead() {
    return head;
  }

  public abstract Set<Edge<T, W>> getAllEdges(SourcedVertex<T, W> vert) throws GraphException;

  @Override
  public Set<Edge<T, W>> getEdges(SourcedVertex<T, W> vert) throws GraphException {
    // when encountering an element on the limit depth, cut
    // it off by returning that it has no neighbors
    if (depthMap.get(vert.getVal()) < maxDepth) {
      return getAllEdges(vert);
    } else {
      return Collections.emptySet();
    }
  }

  /**
   * Initial BFS load-in to properly assign depth numbers.
   * @throws GraphException in the case of erroneous output
   */
  private void loadByBFS() throws GraphException {
    Set<Vertex<T, W>> visited = new HashSet<>();

    try {
      head = this.getVertex(headVal);
    } catch (GraphException e) {
      throw new IllegalStateException("cannot get head vertex. "
          + "Something is seriously broken. ERROR: " + e.getMessage());
    }

    Deque<Vertex<T, W>> queue = new LinkedList<>();
    queue.addLast(head);
    depthMap.put(headVal, 0);

    while (!queue.isEmpty()) {

      Vertex<T, W> cur = queue.pollFirst();
      visited.add(cur);
      int curDepth = depthMap.get(cur.getVal());

      cur.getEdges().stream()
          .forEach(e -> {
            Vertex<T, W> dest = e.getDest();
            T destVal = dest.getVal();

            int destDepth = depthMap.getOrDefault(destVal, curDepth + 1);
            destDepth = Math.min(destDepth, curDepth + 1);
            depthMap.put(destVal, destDepth);
            // only add to the queue if under depth limit
            if (destDepth <= maxDepth && !visited.contains(dest)) {
              queue.addLast(dest);
            }
          });
    }
  }
}


