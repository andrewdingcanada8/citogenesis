package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.algos;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.GraphUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.SimpleEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.SimpleVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.SimpleVertexGenerator;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment.Tarjan;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TarjanTest {

  @Property(trials = 25)
  public void outputSetsAreSCCs(@From(SimpleVertexGenerator.class) SimpleVertex v) throws GraphException {
    List<Set<SimpleVertex>> comps = new Tarjan().search(v);
    // we know a component is an scc if
    // a dfs from every node in the scc contains the scc
    for (Set<SimpleVertex> comp : comps) {
      for (SimpleVertex cv : comp) {
        Set<SimpleVertex> visited = new HashSet<>();
        GraphUtils.dfs(cv, visited);
        assertTrue("not a comp: " + comp + System.lineSeparator() + "in: " + comps,
            visited.containsAll(comp));
      }
    }
  }

  @Property
  public void unionOfCompsAreAllNodesAccessibleFromStart(
      @From(SimpleVertexGenerator.class) SimpleVertex v) throws GraphException {
    List<Set<SimpleVertex>> comps = new Tarjan().search(v);

    // construct set union
    Set<SimpleVertex> union = new HashSet<>();
    comps.stream().forEach(comp -> union.addAll(comp));

    // perform dfs for all accessible vertices
    Set<SimpleVertex> visited = new HashSet<>();
    GraphUtils.dfs(v, visited);

    assertEquals(visited, union);
  }

  @Property
  public void compsAreDisjoint(@From(SimpleVertexGenerator.class) Vertex v) throws GraphException {
    List<Set<Vertex>> comps = new Tarjan().search(v);

    // construct set union
    Set<Vertex> union = new HashSet<>();
    comps.stream().forEach(comp -> union.addAll(comp));

    // add up individual component sizes
    int sumComps = comps.stream().mapToInt(Set::size).sum();

    assertEquals(union.size(), sumComps);
  }

  @Test
  public void threeCycleTest() throws GraphException {
    SimpleVertex v1 = new SimpleVertex("A");
    SimpleVertex v2 = new SimpleVertex("B");
    SimpleVertex v3 = new SimpleVertex("C");
    v1.setEdges(Set.of(new SimpleEdge(0, v1, v2)));
    v2.setEdges(Set.of(new SimpleEdge(0, v2, v3)));
    v3.setEdges(Set.of(new SimpleEdge(0, v3, v1)));
    List<Set<Vertex>> res = new Tarjan<>().search((Vertex)v1);
    assertEquals(res.size(), 1);
    assertTrue(res.get(0).containsAll(Set.of(v1, v2, v3)));
    assertTrue(Set.of(v1, v2, v3).containsAll(res.get(0)));
  }

}
