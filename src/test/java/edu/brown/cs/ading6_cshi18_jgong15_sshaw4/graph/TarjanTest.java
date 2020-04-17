package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.SimpleVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj.SimpleVertexGenerator;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment.Tarjan;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TarjanTest {

  @Property
  public void outputSetsAreSCCs(@From(SimpleVertexGenerator.class) SimpleVertex v) throws GraphException {
    Set<Set<SimpleVertex>> comps = new Tarjan().search(v);
    // we know a component is an scc if
    // a dfs from every node in the scc contains the scc
    for (Set<SimpleVertex> comp : comps) {
      for (SimpleVertex cv : comp) {
        Set<SimpleVertex> visited = new HashSet<>();
        SimpleVertex.dfs(cv, visited);
        assertTrue("not a comp: " + comp + System.lineSeparator() + "in: " + comps,
            visited.containsAll(comp));
      }
    }
  }

  @Property
  public void unionOfCompsAreAllNodesAccessibleFromStart(
      @From(SimpleVertexGenerator.class) SimpleVertex v) throws GraphException {
    Set<Set<SimpleVertex>> comps = new Tarjan().search(v);

    // construct set union
    Set<SimpleVertex> union = new HashSet<>();
    comps.stream().forEach(comp -> union.addAll(comp));

    // perform dfs for all accessible vertices
    Set<SimpleVertex> visited = new HashSet<>();
    SimpleVertex.dfs(v, visited);

    assertEquals(visited, union);
  }

  @Property
  public void compsAreDisjoint(@From(SimpleVertexGenerator.class) Vertex v) throws GraphException {
    Set<Set<Vertex>> comps = new Tarjan().search(v);

    // construct set union
    Set<Vertex> union = new HashSet<>();
    comps.stream().forEach(comp -> union.addAll(comp));

    // add up individual component sizes
    int sumComps = comps.stream().mapToInt(Set::size).sum();

    assertEquals(union.size(), sumComps);
  }

}
