package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.algos;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.path.AStar;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.path.Dijkstra;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree.DimensionException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.kdtree.Point;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit tests for AStar search algorithm (along with Dijkstra for sanity-checking).
 */
public class AStarTest {

  @Test
  public void basicTriangleTest() throws GraphException {
    CoordVertex A = new CoordVertex(0, 0);
    CoordVertex B = new CoordVertex(1, 0);
    CoordVertex C = new CoordVertex(1, 1);

    Edge<Point, Object> AB = new DistEdge(A, B);
    Edge<Point, Object> BC = new DistEdge(B, C);
    Edge<Point, Object> AC = new DistEdge(A, C);

    A.setEdges(Set.of(AB, AC));
    B.setEdges(Set.of(BC));

    assertEquals(new Dijkstra().search(A, new Point(1, 1)), List.of(AC));
    assertEquals(new AStar().search(A, new Point(1, 1)), List.of(AC));
  }

  @Test
  public void searchRespectsDirectenessTest() throws GraphException {
    CoordVertex A = new CoordVertex(0, 0);
    CoordVertex B = new CoordVertex(1, 0);
    CoordVertex C = new CoordVertex(1, 1);

    Edge<Point, Object> AB = new DistEdge(A, B);
    Edge<Point, Object> BC = new DistEdge(B, C);
    Edge<Point, Object> CA = new DistEdge(C, A);

    A.setEdges(Set.of(AB));
    B.setEdges(Set.of(BC));
    C.setEdges(Set.of(CA));

    assertEquals(new Dijkstra().search(A, new Point(1, 1)), List.of(AB, BC));
    assertEquals(new AStar().search(A, new Point(1, 1)), List.of(AB, BC));
  }

  @Test
  public void returnEmptyOnNoPathTest() throws GraphException {
    CoordVertex A = new CoordVertex(0, 0);
    CoordVertex B = new CoordVertex(1, 0);
    CoordVertex C = new CoordVertex(1, 1);
    CoordVertex D = new CoordVertex(-1, -1);

    Edge<Point, Object> AB = new DistEdge(A, B);
    Edge<Point, Object> BC = new DistEdge(B, C);
    Edge<Point, Object> AC = new DistEdge(A, C);
    Edge<Point, Object> DA = new DistEdge(D, A);

    A.setEdges(Set.of(AB, AC));
    B.setEdges(Set.of(BC));
    D.setEdges(Set.of(DA));

    assertTrue(new Dijkstra().search(A, new Point(-1, -1)).isEmpty());
    assertTrue(new AStar().search(A, new Point(-1, -1)).isEmpty());
  }

  @Test
  public void returnEmptyWhenCalledOnSelfTest() throws GraphException {
    CoordVertex A = new CoordVertex(0, 0);
    CoordVertex B = new CoordVertex(1, 0);
    CoordVertex C = new CoordVertex(1, 1);

    Edge<Point, Object> AB = new DistEdge(A, B);
    Edge<Point, Object> BC = new DistEdge(B, C);
    Edge<Point, Object> CA = new DistEdge(C, A);

    A.setEdges(Set.of(AB));
    B.setEdges(Set.of(BC));
    C.setEdges(Set.of(CA));

    assertTrue(new Dijkstra().search(A, new Point(0, 0)).isEmpty());
    assertTrue(new AStar().search(A, new Point(0, 0)).isEmpty());
  }

  @Test
  public void canHandleDeadEndsTest() throws GraphException {
    CoordVertex A = new CoordVertex(0, 0);
    CoordVertex B = new CoordVertex(1, 0);
    CoordVertex C = new CoordVertex(3, 0);

    Edge<Point, Object> AB = new DistEdge(A, B);
    Edge<Point, Object> AC = new DistEdge(A, C);

    A.setEdges(Set.of(AB, AC));

    assertEquals(new Dijkstra().search(A, new Point(3, 0)), List.of(AC));
    assertEquals(new AStar().search(A, new Point(3, 0)), List.of(AC));
  }

  @Test
  public void stronglyConnectedGraphTest() throws GraphException {
    CoordVertex A = new CoordVertex(0, 0);
    CoordVertex B = new CoordVertex(1, 0);
    CoordVertex C = new CoordVertex(0, 7);
    CoordVertex D = new CoordVertex(1, 1);

    Edge<Point, Object> AB = new DistEdge(A, B);
    Edge<Point, Object> AC = new DistEdge(A, C);
    Edge<Point, Object> BC = new DistEdge(B, C);
    Edge<Point, Object> CB = new DistEdge(C, B);
    Edge<Point, Object> BD = new DistEdge(B, D);
    Edge<Point, Object> CD = new DistEdge(C, D);
    Edge<Point, Object> DA = new DistEdge(D, A);

    A.setEdges(Set.of(AB, AC));
    B.setEdges(Set.of(BC, BD));
    C.setEdges(Set.of(CB, CD));
    D.setEdges(Set.of(DA));

    assertEquals(new Dijkstra().search(A, new Point(1, 1)), List.of(AB, BD));
    assertEquals(new AStar().search(A, new Point(1, 1)), List.of(AB, BD));
  }


  protected class CoordVertex implements Vertex<Point, Object> {
    private Point point;
    private Set<Edge<Point, Object>> edges;

    public CoordVertex(double... coords) {
      point = new Point(coords);
      edges = Collections.emptySet();
    }
    @Override
    public Point getVal() {
      return point;
    }
    @Override
    public Set<Edge<Point, Object>> getEdges() throws GraphException {
      return edges;
    }

    public void setEdges(Set<Edge<Point, Object>> edges) {
      this.edges = edges;
    }
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CoordVertex that = (CoordVertex) o;
      return Objects.equals(point, that.point) &&
          Objects.equals(edges, that.edges);
    }
    @Override
    public int hashCode() {
      return Objects.hash(point, edges);
    }

    @Override
    public String toString() {
      return "CoordVertex{" +
          "point=" + point +
          '}';
    }
  }


  protected class DistEdge implements Edge<Point, Object> {
    CoordVertex source;
    CoordVertex dest;
    public DistEdge(CoordVertex source, CoordVertex dest) {
      this.source = source;
      this.dest = dest;
    }
    @Override
    public Object getValue() {
      return null;
    }
    @Override
    public double getWeight() throws GraphException {
      try {
        return source.getVal().distanceTo(dest.getVal());
      } catch (DimensionException e) {
        throw new GraphException("shouldn't be here...");
      }
    }
    @Override
    public Vertex<Point, Object> getSource() {
      return source;
    }
    @Override
    public Vertex<Point, Object> getDest() {
      return dest;
    }

    @Override
    public String toString() {
      return "DistEdge{" +
          "source=" + source +
          ", dest=" + dest +
          '}';
    }
  }

}
