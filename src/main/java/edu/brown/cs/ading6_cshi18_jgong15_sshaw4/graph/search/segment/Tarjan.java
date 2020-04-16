package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.*;
import java.util.stream.Collectors;

public class Tarjan<T, W> implements ComponentSearch<T, W> {

  private int index;
  private Deque<Vertex<T, W>> stack;
  private Set<Set<Vertex<T, W>>> sccs;
  private Map<Vertex<T, W>, Integer> disc;
  private Map<Vertex<T, W>, Integer> lowlink;

  @Override
  public Set<Set<Vertex<T, W>>> search(Vertex<T, W> start) throws GraphException {
    // reset global variables
    index = 0;
    stack = new ArrayDeque<>();
    sccs = new HashSet<>();
    disc = new HashMap<>();
    lowlink = new HashMap<>();
    tarjanEngine(start);
    return sccs;
  }

  private void tarjanEngine(Vertex<T, W> v) throws GraphException {
    // set discovery index to the smallest unused index
    disc.put(v, index);
    lowlink.put(v, index);
    index++;
    stack.addFirst(v);

    // check v neighbors
    Set<Vertex<T, W>> nvs = v.getEdges().stream().map(Edge::getDest).collect(Collectors.toSet());
    for (Vertex<T, W> nv : nvs) {
      if (!disc.containsKey(nv)) {
        // if we haven't visited neighbor, recur
        tarjanEngine(nv);
      } else if (stack.contains(nv)) {
        // if we have, and if on the stack (not part of SCC), nv may be
        // the lowlink node
        lowlink.put(v, Math.min(lowlink.get(v), disc.get(nv)));
      }
    }

    // if they are the same, we got an SCC root
    if (lowlink.get(v) == disc.get(v)) {
      Set<Vertex<T, W>> scc = new HashSet<>();
      // pop stack to get all the nodes
      Vertex<T, W> w;
      do {
        w = stack.removeFirst();
        scc.add(w);
      } while (!v.equals(w));
      sccs.add(scc);
    }

  }
}
