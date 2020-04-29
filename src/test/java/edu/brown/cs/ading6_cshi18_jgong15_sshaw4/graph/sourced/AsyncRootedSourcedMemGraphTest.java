package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.AsyncMatrixRootedMemGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.GraphUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.MatrixRootedMemGraph;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitQuickcheck.class)
public class AsyncRootedSourcedMemGraphTest {

  static final int MAT_SIZE = 100;

  @Property
  public void exposesLessThanDepthTest(boolean
                                       @Size(min = MAT_SIZE, max = MAT_SIZE) []
                                       @Size(min = MAT_SIZE, max = MAT_SIZE) [] mat)
      throws GraphException {
    AsyncMatrixRootedMemGraph graph = new AsyncMatrixRootedMemGraph(mat, 0, 10);
    graph.load();
    Vertex head = graph.getHead();
    Map<Vertex, Integer> dist = new HashMap<>();
    GraphUtils.distBfs(head, dist);

    for (Vertex v : dist.keySet()) {
      assertTrue(dist.get(v) <= 10);
    }
  }

  @Property
  public void depthZeroTest(boolean
                            @Size(min = MAT_SIZE, max = MAT_SIZE) []
                            @Size(min = MAT_SIZE, max = MAT_SIZE) [] mat) throws GraphException {
    AsyncMatrixRootedMemGraph graph = new AsyncMatrixRootedMemGraph(mat, 1, 0);
    graph.load();
    Vertex head = graph.getHead();
    assertTrue(head.getEdges().isEmpty());
  }
}
