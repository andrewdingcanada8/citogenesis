package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleVertex implements Vertex<String, Object>, Cloneable {
  private String name;
  private Set<Edge<String, Object>> edges;

  public SimpleVertex(String name) {
    this.name = name;
    this.edges = Collections.emptySet();
  }

  @Override
  public String getVal() {
    return name;
  }

  @Override
  public Set<Edge<String, Object>> getEdges() {
    return edges;
  }

  public void setEdges(Set<Edge<String, Object>> edges) {
    this.edges = edges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleVertex that = (SimpleVertex) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }


  @Override
  public String toString() {
    Set<SimpleVertex> allVerts = new HashSet<>();
    GraphUtils.dfs(this, allVerts);
    StringBuilder out = new StringBuilder();
    allVerts.forEach(v -> out.append(v.getVal() + " "));
    return out.toString();
  }

  @Override
  public Object clone() {
    HashSet<SimpleVertex> allVerts = new HashSet<>();
    GraphUtils.dfs(this, allVerts);
    // reconstruct vertices
    List<SimpleVertex> newVerts = allVerts.stream()
        .map(v -> new SimpleVertex(v.getVal()))
        .collect(Collectors.toList());
    // reconstruct edges
    allVerts.forEach(oldVert -> {
      //acquire corresponding new vertex
      SimpleVertex newVert = newVerts.get(newVerts.indexOf(oldVert));

      Set<Edge<String, Object>> newEdges = oldVert.edges.stream().map(e -> {
        try {
          return new SimpleEdge(e.getWeight(), newVert, newVerts.get(newVerts.indexOf(e.getDest())));
        } catch (GraphException graphException) {
          throw new IllegalStateException("not supposed to be here...");
        }
      }).collect(Collectors.toSet());
      newVert.setEdges(newEdges);
    });
    // find matching starting edge in newVerts and return it
    return newVerts.get(newVerts.indexOf(this));
  }
}
