package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.*;

public class GraphUtils {
  public static <T extends Vertex> void dfs(T v, Set<T> visited) {
    visited.add(v);
    Set<Edge> es;
    try {
      es = v.getEdges();
    } catch (GraphException e) {
      throw new IllegalStateException("not supposed to be here...");
    }
    es.stream().map(e -> e.getDest())
        .filter(nv -> !visited.contains(nv))
        .forEach(nv -> dfs((T)nv, visited));
  }
  
  public static <T extends Vertex> void distBfs(T v, Map<T, Integer> dist) throws GraphException {
    Deque<Vertex> queue = new LinkedList<>();
    Set<Vertex> visited = new HashSet<>();
    
    dist.put(v, 0);
    queue.addLast(v);
    
    while(!queue.isEmpty()) {
      Vertex cur = queue.pollFirst();
      visited.add(cur);
      Set<Edge> edges = cur.getEdges();
      for (Edge e : edges) {
        Vertex ncur = e.getDest();
        dist.put((T) ncur, Math.min(
            dist.getOrDefault(ncur, dist.get(cur)),
            dist.get(cur)));
        if (!visited.contains(ncur)) {
          queue.addLast(ncur);
        }
      }
    }
  }
}
