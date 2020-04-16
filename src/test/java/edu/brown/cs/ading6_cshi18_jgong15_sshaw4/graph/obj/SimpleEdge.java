package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;

import java.util.Objects;

public class SimpleEdge implements Edge<String, Object> {
  private double weight;
  private Vertex<String, Object> source;
  private Vertex<String, Object> dest;
  public SimpleEdge(double weight, Vertex<String, Object> source, Vertex<String, Object> dest) {
    this.weight = weight;
    this.source = source;
    this.dest = dest;
  }
  @Override
  public Object getValue() {
    return null;
  }
  @Override
  public double getWeight() {
    return weight;
  }
  @Override
  public Vertex<String, Object> getSource() {
    return source;
  }
  @Override
  public Vertex<String, Object> getDest() {
    return dest;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleEdge that = (SimpleEdge) o;
    return Double.compare(that.weight, weight) == 0 &&
        Objects.equals(source, that.source) &&
        Objects.equals(dest, that.dest);
  }
  @Override
  public int hashCode() {
    return Objects.hash(weight, source, dest);
  }
  @Override
  public String toString() {
    return "SimpleEdge{" +
        "weight=" + weight +
        ", source=" + source +
        ", dest=" + dest +
        '}';
  }
}
