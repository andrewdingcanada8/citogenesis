package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class SimpleVertex implements Vertex<String, Object> {
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
  public Set<Edge<String, Object>> getEdges() throws GraphException {
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
    return "SimpleVertex{" +
        "name='" + name + '\'' +
        '}';
  }
}
