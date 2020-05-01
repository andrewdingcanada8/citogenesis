package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.GeneratingSourceFinder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.SourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment.Tarjan;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WebGraphTest {

  @Ignore
  @Test
  public void sanityCheckTest() throws QueryException, GraphException {
    //assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
    SourceQuery sq = new SourceQuery(1);
    Source src = sq.query("https://www.nytimes.com/2020/04/24/us/coronavirus-us-usa-updates.html");
    WebGraph nyGraph = new WebGraph(src, sq, 1);
    nyGraph.getHead();
  }

  @Ignore
  @Test
  public void asyncQuerySanityCheckTest() throws QueryException, GraphException {
    //assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
    AsyncSourceQuery sq = new AsyncSourceQuery(10);
    Source src = sq.query("https://www.nytimes.com/2020/04/26/health/can-antibody-tests-help-end-the-coronavirus-pandemic.html").join();
    AsyncQueryWebGraph nyGraph = new AsyncQueryWebGraph(src, sq, "coronavirus branch", 5);
    nyGraph.load();
    Vertex<Source, String> hv = nyGraph.getHead();
    Collection<Vertex<Source, String>> loadedVertices = nyGraph.getLoadedVertices();
    loadedVertices.stream().forEach(v -> System.out.println("loaded: " + v.getVal().getURL()));

    // conduct search, filter out any dead sources
    List<Set<Vertex<Source, String>>> comps = new Tarjan().search(hv);
    comps = comps.stream()
        .filter(comp ->
            comp.stream().anyMatch(v ->
                v.getVal() instanceof DeadSource))
        .collect(Collectors.toList());

    comps.stream().flatMap(Collection::stream).forEach(v -> v.getVal().queryTimestamp());
    List<Vertex<Source, String>> gens = comps.stream()
        .map(comp -> {
          try {
            return new GeneratingSourceFinder().search(comp);
          } catch (GraphException e) {
            System.out.println("Error while finding gen sources: " + e.getMessage());
            return null;
          }
        })
        .collect(Collectors.toList());
    gens.stream().forEach(v -> System.out.println("generator: " + v));
  }

  @Test
  public void asyncBfsSanityCheckTest() throws QueryException, GraphException {
    //assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
    String key = "that has no specific goals to accomplish, allowing players a large amount of freedom in choosing how to play the game.";
    AsyncSourceQuery sq = new AsyncSourceQuery(30);
    Source src = sq.query("https://www.ign.com/articles/2011/11/24/minecraft-review").join();
    AsyncWebGraph graph = new AsyncWebGraph(src, sq, key,3);
    graph.load();
    Collection<Vertex<Source, String>> loadedVertices = graph.getLoadedVertices();
    loadedVertices.stream().forEach(v -> System.out.println("loaded: " + v.getVal().getURL()));

    Vertex<Source, String> hv = graph.getHead();
    List<Set<Vertex<Source, String>>> comps = new Tarjan().search(hv);

    comps.stream().flatMap(Collection::stream).forEach(v -> v.getVal().queryTimestamp());
    List<Vertex<Source, String>> gens = comps.stream()
        .map(comp -> {
          try {
            return new GeneratingSourceFinder().search(comp);
          } catch (GraphException e) {
            System.out.println("Error while finding gen sources: " + e.getMessage());
            return null;
          }
        })
        .collect(Collectors.toList());
    gens.stream().forEach(v -> System.out.println("generator: " + v));
  }


}
