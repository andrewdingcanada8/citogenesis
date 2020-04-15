package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.Dijkstra;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit tests for Dijkstra search algorithm.
 */
public class DijkstraTest {

  @Test
  public void basicTriangleTest() throws GraphException {
    SimpleVertex A = new SimpleVertex("A");
    SimpleVertex B = new SimpleVertex("B");
    SimpleVertex C = new SimpleVertex("C");

    Edge<String, Object> AB = new SimpleEdge(0.5, A, B);
    Edge<String, Object> BC = new SimpleEdge(0.1, B, C);
    Edge<String, Object> AC = new SimpleEdge(2.0, A, C);

    A.setEdges(Set.of(AB, AC));
    B.setEdges(Set.of(BC));

    assertEquals(new Dijkstra().search(A, "C"), List.of(AB, BC));
  }

  @Test
  public void searchRespectsDirectenessTest() throws GraphException {
    SimpleVertex A = new SimpleVertex("A");
    SimpleVertex B = new SimpleVertex("B");
    SimpleVertex C = new SimpleVertex("C");

    Edge<String, Object> AB = new SimpleEdge(0.5, A, B);
    Edge<String, Object> BC = new SimpleEdge(0.1, B, C);
    Edge<String, Object> CA = new SimpleEdge(0.05, C, A);

    A.setEdges(Set.of(AB));
    B.setEdges(Set.of(BC));
    C.setEdges(Set.of(CA));

    assertEquals(new Dijkstra().search(A, "C"), List.of(AB, BC));
  }

  @Test
  public void returnEmptyOnNoPathTest() throws GraphException {
    SimpleVertex A = new SimpleVertex("A");
    SimpleVertex B = new SimpleVertex("B");
    SimpleVertex C = new SimpleVertex("C");
    SimpleVertex D = new SimpleVertex("D");

    Edge<String, Object> AB = new SimpleEdge(0.5, A, B);
    Edge<String, Object> BC = new SimpleEdge(0.1, B, C);
    Edge<String, Object> AC = new SimpleEdge(2.0, A, C);
    Edge<String, Object> DA = new SimpleEdge(0.1, D, A);

    A.setEdges(Set.of(AB, AC));
    B.setEdges(Set.of(BC));
    D.setEdges(Set.of(DA));

    assertTrue(new Dijkstra().search(A, "D").isEmpty());
  }

  @Test
  public void returnEmptyWhenCalledOnSelfTest() throws GraphException {
    SimpleVertex A = new SimpleVertex("A");
    SimpleVertex B = new SimpleVertex("B");
    SimpleVertex C = new SimpleVertex("C");

    Edge<String, Object> AB = new SimpleEdge(0.5, A, B);
    Edge<String, Object> BC = new SimpleEdge(0.1, B, C);
    Edge<String, Object> CA = new SimpleEdge(0.05, C, A);

    A.setEdges(Set.of(AB));
    B.setEdges(Set.of(BC));
    C.setEdges(Set.of(CA));

    assertTrue(new Dijkstra().search(A, "A").isEmpty());
  }

  @Test
  public void canDealWithDeadEndsTest() throws GraphException {
    SimpleVertex A = new SimpleVertex("A");
    SimpleVertex B = new SimpleVertex("B");
    SimpleVertex C = new SimpleVertex("C");

    Edge<String, Object> AB = new SimpleEdge(0.5, A, B);
    Edge<String, Object> AC = new SimpleEdge(1.0, A, C);

    A.setEdges(Set.of(AB, AC));

    assertEquals(new Dijkstra().search(A, "C"), List.of(AC));

  }

  @Test
  public void resilientToLocalBaitingTest() throws GraphException {
    SimpleVertex A = new SimpleVertex("A");
    SimpleVertex B = new SimpleVertex("B");
    SimpleVertex BP = new SimpleVertex("BPrime");
    SimpleVertex C = new SimpleVertex("C");
    SimpleVertex CP = new SimpleVertex("CPrime");
    SimpleVertex D = new SimpleVertex("D");

    Edge<String, Object> AB = new SimpleEdge(1, A, B);
    Edge<String, Object> BC = new SimpleEdge(1, B, C);
    Edge<String, Object> CD = new SimpleEdge(1, C, D);

    Edge<String, Object> BBP = new SimpleEdge(0.5, B, BP);
    Edge<String, Object> CCP = new SimpleEdge(0.5, C, CP);

    Edge<String, Object> AD = new SimpleEdge(2.999, A, D);

    A.setEdges(Set.of(AB, AD));
    B.setEdges(Set.of(BC, BBP));
    C.setEdges(Set.of(CD, CCP));

    assertEquals(new Dijkstra().search(A, "D"), List.of(AD));
  }

  @Test
  public void stronglyConnectedGraphTest() throws GraphException {
    SimpleVertex A = new SimpleVertex("A");
    SimpleVertex B = new SimpleVertex("B");
    SimpleVertex C = new SimpleVertex("C");
    SimpleVertex D = new SimpleVertex("D");

    Edge<String, Object> AB = new SimpleEdge(0.1, A, B);
    Edge<String, Object> AC = new SimpleEdge(1.0, A, C);
    Edge<String, Object> BC = new SimpleEdge(0.1, B, C);
    Edge<String, Object> CB = new SimpleEdge(1.0, C, B);
    Edge<String, Object> BD = new SimpleEdge(1.0, B, D);
    Edge<String, Object> CD = new SimpleEdge(0.1, C, D);
    Edge<String, Object> DA = new SimpleEdge(0.05, D, A);

    A.setEdges(Set.of(AB, AC));
    B.setEdges(Set.of(BC, BD));
    C.setEdges(Set.of(CB, CD));
    D.setEdges(Set.of(DA));

    assertEquals(new Dijkstra().search(A, "D"), List.of(AB, BC, CD));
  }


  static class SimpleEdge implements Edge<String, Object> {
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

  static class SimpleVertex implements Vertex<String, Object> {
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
          "name='" + name + "\'}";
    }
  }
}
