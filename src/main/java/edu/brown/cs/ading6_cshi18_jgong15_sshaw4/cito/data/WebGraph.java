package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Deadend;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.util.*;

public class WebGraph extends RootedSourcedMemGraph<Source, String> {

  public static final int DEFAULT_DEPTH = 10;
  private Query<String, Source> srcQuery;

  public WebGraph(Source headVal, Query<String, Source> srcQuery) {
    this(headVal, srcQuery, DEFAULT_DEPTH);
  }
  public WebGraph(Source headVal, Query<String, Source> srcQuery, int depth) {
    super(headVal, depth);
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
        Vertex<Source, String> nVert;
        WebSource dummy = new WebSource(url, "", null);
        // if loaded already, acquire new page without querying soure
        if (this.loadedVertex(dummy)) {
          nVert = this.getVertex(dummy);
        } else {
          Source nSrc = srcQuery.query(url);
          nVert = this.getVertex(nSrc);
        }
        neighbors.add(new SourcedEdge<>(url, 0.0, vert, nVert));
        System.out.println("adding... " + url);
      } catch (QueryException | IllegalArgumentException e) {
        // do nothing (i.e, don't attach anything to the source)
        neighbors.add(new SourcedEdge<>(url, 0.0, vert, new Deadend<>(new DeadSource(url))));
        System.out.println("Graph search encountered error: " + e.getMessage());
      }
    }
    return neighbors;
  }
}
