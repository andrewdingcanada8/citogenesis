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

/**
 * An implementation of a self-exploring rooted graph using a synchronous BFS
 * search algorithm.
 * @param <T> type stored in Vertex
 * @param <W> type stored in Edge
 */
public abstract class RootedSourcedMemGraph<T, W> extends SourcedMemGraph<T, W> {
  // rather than extension, this behavior may be integrated by composition
  // I'll stick with this now, but will get back to it if there's time

  private static final int MAX_DEPTH_DEFAULT = 10;

  private T headVal;
  private Vertex<T, W> head;
  private Map<T, Integer> depthMap;
  private int maxDepth;

  /**
   * Constructs a new RootedSourcedMemGraph with default timeout.
   * @param headVal root value from which to conduct search
   */
  public RootedSourcedMemGraph(T headVal) {
    this(headVal, MAX_DEPTH_DEFAULT);
  }

  /**
   * Constructs a new RootedSourcedMemGraph with specified timeout.
   * @param headVal root value from which to conduct search
   * @param maxDepth maximum depth of the graph
   */
  public RootedSourcedMemGraph(T headVal, int maxDepth) {
    this.headVal = headVal;
    this.maxDepth = maxDepth;
    if (maxDepth < 0) {
      maxDepth = 0;
    }
    depthMap = new HashMap<>();
    head = this.getVertex(headVal);
  }

  /**
   * Start graph exploration process.
   * @throws GraphException Exception while obtaining neighboring vertices and edges
   */
  public void load() throws GraphException {
    this.loadByBFS();
  }

  /**
   * Return root value.
   * @return root value stored at root vertex
   */
  public Vertex<T, W> getHead() {
    return head;
  }

  /**
   * Given a specific vertex, return associated neighboring edges and vertices.
   * @param vert vertex to process
   * @return neighboring edges
   * @throws GraphException Exception while parsing source
   */
  public abstract Set<Edge<T, W>> getAllEdges(SourcedVertex<T, W> vert) throws GraphException;

  /**
   * If within depth, return vertex edges, otherwise returns empty set.
   * @param vert vertex to process
   * @return set of edges if under depth limit, empty set otherwise
   * @throws GraphException Exception while parsing source
   */
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
   * @return head value stored in root vertex
   */
  public T getHeadVal() {
    return headVal;
  }

  /**
   * @return max depth
   */
  protected int getMaxDepth() {
    return maxDepth;
  }

  /**
   * @return map containing depth of each vertex
   */
  protected Map<T, Integer> getDepthMap() {
    return depthMap;
  }

  /**
   * Synchronous BFS load in vertices.
   * @throws GraphException Exception while obtaining neighboring edges
   */
  protected void loadByBFS() throws GraphException {
    Set<Vertex<T, W>> visited = new HashSet<>();
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


