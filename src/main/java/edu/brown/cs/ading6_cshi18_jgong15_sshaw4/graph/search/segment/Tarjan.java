package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of Tarjan's algorithm.
 * @param <T> type stored in Vertex
 * @param <W> type stored in Edge
 */
public class Tarjan<T, W> implements ComponentSearch<T, W> {

  private int index;
  private Deque<Vertex<T, W>> stack;
  private List<Set<Vertex<T, W>>> sccs;
  private Map<Vertex<T, W>, Integer> disc;
  private Map<Vertex<T, W>, Integer> lowlink;

  /**
   * Given a Vertex, return a list of strongly-connected components of all
   * vertices accessible from that vertex.
   * @param start Vertex from which to start segmentation
   * @return List of SCCs
   * @throws GraphException Exception while obtaining neighboring edges and vertices
   */
  @Override
  public List<Set<Vertex<T, W>>> search(Vertex<T, W> start) throws GraphException {
    // reset global variables
    index = 0;
    stack = new ArrayDeque<>();
    sccs = new ArrayList<>();
    disc = new HashMap<>();
    lowlink = new HashMap<>();
    tarjanEngine(start);
    return sccs;
  }

  /**
   * Recursive component of Tarjan's algorithm: the fancy depth-first search.
   * @param v Vertex to be processed
   * @throws GraphException Exception while obtaining neighboring vertices
   */
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
        lowlink.put(v, Math.min(lowlink.get(v), lowlink.get(nv)));
      } else if (stack.contains(nv)) {
        // if we have, and if on the stack (not part of SCC), nv may be
        // the lowlink node
        lowlink.put(v, Math.min(lowlink.get(v), disc.get(nv)));
      }
    }

    // if they are the same, we got an SCC root
    if (lowlink.get(v).equals(disc.get(v))) {
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
