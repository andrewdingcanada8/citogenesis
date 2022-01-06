package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.AsyncRootedSourcedMemGraph;

import java.util.HashSet;
import java.util.Set;

public class AsyncMatrixRootedMemGraph extends AsyncRootedSourcedMemGraph<Integer, Object> {
  private boolean[][] mat;

  public AsyncMatrixRootedMemGraph(boolean[][] mat, Integer headVal, int depth) {
    super(headVal, depth);
    this.mat = mat;
  }

  @Override
  public Set<Edge<Integer, Object>> getAllEdges(SourcedVertex<Integer, Object> vert) throws GraphException {
    int vertId = vert.getVal();
    Set<Edge<Integer, Object>> edges = new HashSet<>();
    for (int i = 0; i < mat[vertId].length; i++) {
      if (mat[vertId][i]) {
        edges.add(new SourcedEdge<>(null, 0, vert, this.getVertex(i)));
      }
    }
    return edges;
  }
}
