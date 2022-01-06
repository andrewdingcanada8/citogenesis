package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * An implementation of RootedSourcedMemGraph that uses a concurrent shared-memory
 * approach to the BFS exploration algorithm.
 *
 * @param <T> type stored in Vertex
 * @param <W> type stored in Edge
 */
public abstract class AsyncRootedSourcedMemGraph<T, W> extends RootedSourcedMemGraph<T, W> {

  public static final int NUM_THREADS = 3;
  private ExecutorService pool;

  /**
   * Constructs a new AsyncRootedSourcedMemGraph.
   *
   * @param headVal  stored value for root Vertex
   * @param maxDepth maximum search depth
   */
  public AsyncRootedSourcedMemGraph(T headVal, int maxDepth) {
    super(headVal, maxDepth);
    pool = Executors.newFixedThreadPool(NUM_THREADS);
  }

  /**
   * Concurrent BFS search algorithm.
   *
   * @throws GraphException Exception while obtaining neighboring vertices and edges.
   */
  @Override
  protected void loadByBFS() throws GraphException {
    Map<T, Integer> dMap = this.getDepthMap();
    Vertex<T, W> head = this.getHead();
    dMap.put(head.getVal(), 0);
    Set<Vertex<T, W>> fringe = new HashSet<>();
    fringe.add(head);
    for (int i = 0; i <= this.getMaxDepth(); i++) {
      Set<CompletableFuture<Set<Vertex<T, W>>>> frntVertFuts = fringe.stream()
          .map(v -> vertExplore(v))
          .collect(Collectors.toSet());
      // wait till frontier exploration is done
      CompletableFuture.allOf(frntVertFuts.toArray(CompletableFuture[]::new)).join();
      fringe = frntVertFuts.stream()
          .flatMap(fut -> fut.join().stream())
          .collect(Collectors.toSet());
      int finalI = i;
      fringe.stream()
          .forEach(v -> {
            dMap.put(v.getVal(), Math.min(dMap.getOrDefault(v.getVal(), finalI), finalI));
          });
    }
  }

  /**
   * Returns reference to results of asynchronous vertex exploration process.
   *
   * @param v vertex of which to explore edges
   * @return reference to finished edge computation
   */
  private CompletableFuture<Set<Vertex<T, W>>> vertExplore(Vertex<T, W> v) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return v.getEdges().stream().map(e -> e.getDest()).collect(Collectors.toSet());
      } catch (GraphException e) {
        System.err.println("Graph exploration error: " + e.getMessage());
        return Collections.emptySet();
      }
    }, pool);

  }
}
