package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedGraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of an unrooted graph derived from an external source.
 *
 * @param <T> type stored in Vertex
 * @param <W> type stored in Edge
 */
public abstract class SourcedMemGraph<T, W> extends SourcedGraph<T, W> {


  private Map<T, Vertex<T, W>> vertices;

  /**
   * Constructs a new SourcedMemGraph.
   */
  public SourcedMemGraph() {
    this.vertices = new HashMap<>();
  }

  /**
   * @param val value stored in a {@link Vertex} in graph
   * @return Vertex in this instance containing val
   */
  @Override
  public synchronized Vertex<T, W> getVertex(T val) {

    if (vertices.keySet().contains(val)) {
      return vertices.get(val);
    }
    // if not present, construct a new instance and add to graph
    Vertex<T, W> newVert = new SourcedMemVertex(val, this);
    vertices.put(val, newVert);
    return newVert;
  }

  /**
   * @param val value that can be stored in Vertex
   * @return if graph contains a Vertex with val
   */
  protected boolean loadedVertex(T val) {
    return vertices.keySet().contains(val);
  }
  /**
   * @return collection of vertices loaded in this graph
   */
  public Collection<Vertex<T, W>> getLoadedVertices() {
    return vertices.values();
  }
}
