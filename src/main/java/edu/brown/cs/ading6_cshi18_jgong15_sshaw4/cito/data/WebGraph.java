package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.SourcedMemGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebGraph extends SourcedMemGraph<Source, String> {

  private Query<String, Source> srcQuery;
  private int depthLimit;

  public WebGraph(Query<String, Source> srcQuery) {
    this.srcQuery = srcQuery;
    this.depthLimit = -1;
  }

  public WebGraph(Query<String, Source> srcQuery, int depthLimit) {
    this.srcQuery = srcQuery;
    this.depthLimit = depthLimit;
  }
  @Override
  public Set<Edge<Source, String>> getEdges(SourcedVertex<Source, String> vert) throws GraphException {
    Source src = vert.getVal();
    List<String> links = src.getLinks();
    Set<Edge<Source, String>> neighbors = new HashSet<>();
    for (String url : links) {
      try {
        Source nSrc = srcQuery.query(url);
        Vertex<Source, String> nVert = this.getVertex(nSrc);

        // if the depth limit was set...
        // TODO: currently, if revisited with new depth, new depth is not updated
        // through the graph
        if (depthLimit > 0) {
          if (src.getDepth() > -1) {             // if src has a set depth...
            if (nSrc.getDepth() < 0) {
              nSrc.setDepth(src.getDepth() + 1); // if nSrc depth unset, then set to src's + 1
            } else {
              nSrc.setDepth(Math.min(src.getDepth() + 1, nSrc.getDepth()));
            }
          }
          if (nSrc.getDepth() > depthLimit) {
            continue;
          }
        }
        neighbors.add(new SourcedEdge<>(url, 0.0, vert, nVert));
      } catch (QueryException e) {
        // do nothing
      }
    }
    return neighbors;
  }
}
