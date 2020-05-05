package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Deadend;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * An implementation of RootedSourcedMemGraph that uses a concurrent shared-memory
 * approach to the BFS exploration algorithm.
 *
 * @param <T>
 * @param <W>
 */
public abstract class AsyncRootedSourcedMemGraph<T, W> extends RootedSourcedMemGraph<T, W> {

  public AsyncRootedSourcedMemGraph(T headVal, int maxDepth) {
    super(headVal, maxDepth);
  }

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

  private CompletableFuture<Set<Vertex<T, W>>> vertExplore(Vertex<T, W> v) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return v.getEdges().stream().map(e -> e.getDest()).collect(Collectors.toSet());
      } catch (GraphException e) {
        System.err.println("Graph exploration error: " + e.getMessage());
        return Set.of(new Deadend<>(null));
      }
    });

  }
}
