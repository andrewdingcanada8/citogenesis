package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.algos;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.GraphUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.SimpleVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.SimpleVertexGenerator;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.StronglyConnectedSimpleGenerator;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.components.GeneratingVertexFinder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment.Tarjan;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class GeneratingVertexTest {

  @Property
  public void ifGenFoundItsInComp(@From(SimpleVertexGenerator.class) SimpleVertex v) throws GraphException {
    Set<Set<Vertex<String, Object>>> comps = new Tarjan().search(v);
    for (Set<Vertex<String, Object>> comp : comps) {
      Vertex<String, Object> gen = new MinStringFinder().search(comp);
      if (gen != null) {
        assertTrue(comp.contains(gen));
      }
    }

  }

  @Property
  public void isMin(@From(SimpleVertexGenerator.class) SimpleVertex v) throws GraphException {
    Set<Set<Vertex<String, Object>>> comps = new Tarjan().search(v);
    for (Set<Vertex<String, Object>> comp : comps) {
      Vertex<String, Object> gen = new MinStringFinder().search(comp);
      if (gen != null) {
        assertEquals(Collections.min(comp, Comparator.comparing(Vertex::getVal)), gen);
      }
    }
  }

  @Property
  public void ifReturnIsNullMinIsNotConnected(@From(SimpleVertexGenerator.class) SimpleVertex v) throws GraphException {
    Set<Set<Vertex<String, Object>>> comps = new Tarjan().search(v);
    for (Set<Vertex<String, Object>> comp : comps) {
      Vertex<String, Object> gen = new MinStringFinder().search(comp);
      if (gen == null) {
        Vertex<String, Object> min = Collections.min(comp, Comparator.comparing(Vertex::getVal));
        boolean hasExternalSources = min.getEdges().stream().anyMatch(e -> !comp.contains(e.getDest()));
        assertTrue(comp.toString(), hasExternalSources);
      }
    }

  }

  private class MinStringFinder implements GeneratingVertexFinder<String, Object> {
    @Override
    public Comparator<String> getCompr() {
      return String::compareTo;
    }
  }
}
