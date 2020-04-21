package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedGraph;

import java.util.HashMap;
import java.util.Map;

public abstract class SourcedMemGraph<T, W> extends SourcedGraph<T, W> {

  private Map<T, Vertex<T, W>> vertices;

  public SourcedMemGraph() {
    this.vertices = new HashMap<>();
  }

  @Override
  public Vertex getVertex(T val) throws GraphException {
    // may bite us later, but if you can't find vertex in map, construct
    // it here and just add it to the graph
    if (vertices.keySet().contains(val)) {
      return vertices.get(val);
    }
    // if not present, construct a new instance and add to graph
    Vertex<T, W> newVert = new SourcedMemVertex(val, this);
    vertices.put(val, newVert);
    return newVert;
  }
}
