package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.util.*;

public class WebGraph extends RootedSourcedMemGraph<Source, String> {

  private Query<String, Source> srcQuery;

  public WebGraph(Source headVal, Query<String, Source> srcQuery) throws GraphException {
    super(headVal);
    this.srcQuery = srcQuery;
  }


  @Override
  public Set<Edge<Source, String>> getAllEdges(SourcedVertex<Source, String> vert)
      throws GraphException {
    Source src = vert.getVal();
    List<String> links = src.getLinks();
    Set<Edge<Source, String>> neighbors = new HashSet<>();
    for (String url : links) {
      try {
        Source nSrc = srcQuery.query(url);
        Vertex<Source, String> nVert = this.getVertex(nSrc);
        neighbors.add(new SourcedEdge<>(url, 0.0, vert, nVert));
      } catch (QueryException e) {
        // do nothing
      }
    }
    return neighbors;
  }
}
