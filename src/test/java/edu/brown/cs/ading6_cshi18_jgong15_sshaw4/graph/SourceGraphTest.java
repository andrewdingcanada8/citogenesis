package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.SourceParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.GraphSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit testing SourcedGraph implementation
 */
public class SourceGraphTest {

  @Test
  public void minimalThreeNodeTest() throws GraphException {
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");

    a.setConnNodes(Set.of(b, c));
    b.setConnNodes(Set.of(a, c));
    c.setConnNodes(Set.of(a, b));

    Line ab = new Line("AB");
    Line bc = new Line("BC");
    Line ac = new Line("AC");
    
    GraphSource<Node, Line> mSource = new MiniSource(Set.of(a, b, c), Set.of(ab, bc, ac));
    Graph<Node, Line> graph = new SourcedGraph<>(mSource);

    Vertex<Node, Line>vertA = graph.getVertex(a);
    Vertex<Node, Line>vertB = graph.getVertex(b);
    Vertex<Node, Line>vertC = graph.getVertex(c);

    assertEquals(vertA, new SourcedVertex<>(a, mSource));
    assertEquals(vertB, new SourcedVertex<>(b, mSource));
    assertEquals(vertC, new SourcedVertex<>(c, mSource));

    Set<Edge<Node, Line>> edgesA = Set.of(
        new SourcedEdge<>(ab, 0, vertA, vertB), new SourcedEdge(ac, 0, vertA, vertC));
    Set<Edge<Node, Line>> edgesB = Set.of(
        new SourcedEdge(bc, 0, vertB, vertC), new SourcedEdge(ab, 0, vertB, vertA));
    Set<Edge<Node, Line>> edgesC = Set.of(
        new SourcedEdge(ac, 0, vertC, vertA), new SourcedEdge(bc, 0, vertC, vertB));

    assertEquals(vertA.getEdges(), edgesA);
    assertEquals(vertB.getEdges(), edgesB);
    assertEquals(vertC.getEdges(), edgesC);
  }

  @Test
  public void extraThreeNodeTest() throws GraphException {
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");
    Node d = new Node("D");

    a.setConnNodes(Set.of(b, c));
    b.setConnNodes(Set.of(a, c));
    c.setConnNodes(Set.of(a, b));

    Line ab = new Line("AB");
    Line bc = new Line("BC");
    Line ac = new Line("AC");
    Line ad = new Line("AD");
    Line bd = new Line("BD");
    Line cd = new Line("CD");


    GraphSource<Node, Line> mSource = new MiniSource(Set.of(a, b, c, d), Set.of(ab, bc, ac, ad, bd, cd));
    Graph<Node, Line> graph = new SourcedGraph<>(mSource);

    Vertex<Node, Line>vertA = graph.getVertex(a);
    Vertex<Node, Line>vertB = graph.getVertex(b);
    Vertex<Node, Line>vertC = graph.getVertex(c);

    assertEquals(vertA, new SourcedVertex<>(a, mSource));
    assertEquals(vertB, new SourcedVertex<>(b, mSource));
    assertEquals(vertC, new SourcedVertex<>(c, mSource));

    Set<Edge<Node, Line>> edgesA = Set.of(
        new SourcedEdge<>(ab, 0, vertA, vertB), new SourcedEdge(ac, 0, vertA, vertC));
    Set<Edge<Node, Line>> edgesB = Set.of(
        new SourcedEdge(bc, 0, vertB, vertC), new SourcedEdge(ab, 0, vertB, vertA));
    Set<Edge<Node, Line>> edgesC = Set.of(
        new SourcedEdge(ac, 0, vertC, vertA), new SourcedEdge(bc, 0, vertC, vertB));

    assertEquals(vertA.getEdges(), edgesA);
    assertEquals(vertB.getEdges(), edgesB);
    assertEquals(vertC.getEdges(), edgesC);
  }

  @Test
  public void selfEdgeTest() throws GraphException {
    Node a = new Node("A");
    a.setConnNodes(Set.of(a));

    Line aa = new Line("AA");

    GraphSource mSource = new MiniSource(Set.of(a), Set.of(aa));
    Graph<Node, Line> graph = new SourcedGraph<>(mSource);

    Vertex<Node, Line> vertA = graph.getVertex(a);
    assertFalse(vertA.getEdges().isEmpty());
  }

  @Test
  public void noSelfEdgeTest() throws GraphException {
    Node a = new Node("A");
    a.setConnNodes(Set.of());

    Line aa = new Line("AA");

    GraphSource mSource = new MiniSource(Set.of(a), Set.of(aa));
    Graph<Node, Line> graph = new SourcedGraph<>(mSource);

    Vertex<Node, Line> vertA = graph.getVertex(a);
    assertTrue(vertA.getEdges().isEmpty());
  }

  private class Node {
    Set<Node> connNodes;
    String name;
    public Node(String name) {
      this.name = name;
      connNodes = new HashSet<>();
    }

    public boolean compatibleWith(Node n) {
      return connNodes.contains(n);
    }
    public void setConnNodes(Set<Node> nodes) {
      connNodes = nodes;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Node node = (Node) o;
      return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }

    @Override
    public String toString() {
      return "Node{" +
          ", name='" + name + '\'' +
          '}';
    }
  }

  private class Line {
    String containingString;
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Line line = (Line) o;
      return Objects.equals(containingString, line.containingString);
    }
    @Override
    public int hashCode() {
      return Objects.hash(containingString);
    }
    public Line (String containingString) {
      this.containingString = containingString;
    }

    public double getWeight() {
      return 0;
    }

    public boolean isConnected(Node node) {
      return containingString.contains(node.name);
    }

    @Override
    public String toString() {
      return "Line{" +
          "containingString='" + containingString + '\'' +
          '}';
    }
  }

  private class MiniSource implements GraphSource<Node, Line> {
    Set<Node> nodes;
    Set<Line> lines;
    public MiniSource(Set<Node> ns, Set<Line> ls) {
      this.nodes = ns;
      this.lines = ls;
    }
    @Override
    public Set<Edge<Node, Line>> getEdges(SourcedVertex<Node, Line> inVert) throws SourceParseException {
      Set<Edge<Node, Line>> out = new HashSet<>();
      Node inNode = inVert.getVal();
      for (Line l : lines) {
        if (l.isConnected(inNode)) {
          for (Node outNode : nodes) {
            if (l.isConnected(outNode) && inNode.compatibleWith(outNode)) {
              SourcedVertex<Node, Line> outVert = new SourcedVertex<>(outNode, this);
              out.add(new SourcedEdge<>(l, 0, inVert, outVert));
            }
          }
        }
      }
      return out;
    }
    
  }
  
}
