package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedGraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class SourcedMemGraph<T, W> extends SourcedGraph<T, W> {

  private Map<T, Vertex<T, W>> vertices;

  public SourcedMemGraph() {
    this.vertices = new HashMap<>();
  }

  @Override
  public Vertex getVertex(T val) {

    if (vertices.keySet().contains(val)) {
      return vertices.get(val);
    }
    // if not present, construct a new instance and add to graph
    Vertex<T, W> newVert = new SourcedMemVertex(val, this);
    vertices.put(val, newVert);
    return newVert;
  }

  protected boolean loadedVertex(T val) {
    return vertices.keySet().contains(val);
  }

  public Collection<Vertex<T, W>> getLoadedVertices() {
    return vertices.values();
  }
}
