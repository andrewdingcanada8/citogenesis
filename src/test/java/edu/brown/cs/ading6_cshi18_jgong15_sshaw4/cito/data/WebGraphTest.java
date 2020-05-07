package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.WebTestUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.MockServerUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph.AsyncQueryWebGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph.AsyncSearchWebGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph.SyncWebGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source.DeadSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.GeneratingSourceFinder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.async.AsyncSourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.SourceQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.search.segment.Tarjan;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assume.assumeTrue;
import static org.junit.Assert.*;

public class WebGraphTest {

  @Ignore
  @Test
  public void sanityCheckTest() throws QueryException, GraphException {
    assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
    SourceQuery sq = new SourceQuery(1);
    Source src = sq.query("https://www.nytimes.com/2020/04/24/us/coronavirus-us-usa-updates.html");
    SyncWebGraph nyGraph = new SyncWebGraph(src, sq, 1);
    nyGraph.getHead();
  }

  @Ignore
  @Test
  public void asyncQuerySanityCheckTest() throws QueryException, GraphException {
    assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
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

  @Ignore
  @Test
  public void asyncBfsSanityCheckTest() throws QueryException, GraphException {
    //assumeTrue(WebTestUtils.checkURL("https://www.nytimes.com/"));
    AsyncSourceQuery sq = new AsyncSourceQuery(10);
    Source src = sq.query("https://www.nytimes.com/2020/05/05/us/jared-kushner-fema-coronavirus.html").join();
    String key = src.getContent();
    AsyncSearchWebGraph graph = new AsyncSearchWebGraph(src, sq, key, 1, 0.8);
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

  @Test
  public void cycleAlgoIntegrationTest() throws QueryException, GraphException {
    assumeTrue(WebTestUtils.HTTP_TIMEOUT > 0);
    Query<String, CompletableFuture<Source>> sq = new AsyncSourceQuery(WebTestUtils.HTTP_TIMEOUT);

    WireMockServer mockServer = new WireMockServer(options().port(8089));
    mockServer.start();
    MockServerUtils.setUpMockServer(8089);
    Source hdSrc = sq.query("http://localhost:63342/"
        + "term-project-ading6-cshi18-jgong15-sshaw4-1/"
        + "dummies/cycle3.html").join();

    assertEquals(hdSrc.title(), "last piece of a 3-cycle");

    AsyncSearchWebGraph webGraph = new AsyncSearchWebGraph(
        hdSrc, sq, hdSrc.getContent(), 5, 0.8);
    webGraph.load();
    Vertex<Source, String> hv = webGraph.getHead();
    List<Set<Vertex<Source, String>>> sccs = new Tarjan<Source, String>().search(hv);

    assertEquals(sccs.size(), 1);
    assertEquals(sccs.get(0).size(), 3);

    Vertex<Source, String> gen = new GeneratingSourceFinder().search(sccs.get(0));
    assertNotNull(gen);
    Source genSrc = gen.getVal();
    assertNotNull(genSrc);

    assertEquals(genSrc.getURL(), "http://localhost:63342/"
            + "term-project-ading6-cshi18-jgong15-sshaw4-1/"
            + "dummies/cycle1.html");
    mockServer.stop();
  }

  @Test
  public void noTimeStampStillWorksTest() throws QueryException, GraphException {
    assumeTrue(WebTestUtils.HTTP_TIMEOUT > 0);
    Query<String, CompletableFuture<Source>> sq = new AsyncSourceQuery(WebTestUtils.HTTP_TIMEOUT);
    Source hdSrc = sq.query("http://localhost:63342/"
        + "term-project-ading6-cshi18-jgong15-sshaw4-1/"
        + "dummies/pair.html").join();

    assertEquals(hdSrc.title(), "Pair");

    AsyncSearchWebGraph webGraph = new AsyncSearchWebGraph(
        hdSrc, sq, hdSrc.getContent(), 5, 0.8);
    webGraph.load();
    Vertex<Source, String> hv = webGraph.getHead();
    List<Set<Vertex<Source, String>>> sccs = new Tarjan<Source, String>().search(hv);

    assertEquals(sccs.size(), 2);
    assertEquals(sccs.get(0).size(), 1);
    assertEquals(sccs.get(1).size(), 1);

    List<Vertex<Source, String>> gens = sccs.stream().map(c -> {
      try {
        return new GeneratingSourceFinder().search(c);
      } catch (GraphException e) {
        System.out.println("serious problem: " + e.getMessage());
        return null;
      }
    }).collect(Collectors.toList());

    assertEquals(gens.size(), 2);
    assertTrue(gens.get(0) == null && gens.get(1) != null
        || gens.get(1) == null && gens.get(0) != null);
    Vertex<Source, String> gen = gens.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList())
        .get(0);
    assertNotNull(gen);

    Source genSrc = gen.getVal();
    assertNotNull(genSrc);
    assertEquals(genSrc.getURL(), "http://localhost:63342/"
        + "term-project-ading6-cshi18-jgong15-sshaw4-1/"
        + "dummies/singleton.html");
  }

}
