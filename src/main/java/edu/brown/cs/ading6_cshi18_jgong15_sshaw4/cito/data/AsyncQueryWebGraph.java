package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source.CosSimThreshold;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source.SourceRule;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.url.HostBlacklistFactory;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.url.NoInLinking;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.url.URLRule;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncQueryWebGraph extends RootedSourcedMemGraph<Source, String> {
  public static final Set<URLRule> URL_RULES;
  private String keywords;
  private Source keySource;
  static {
    URL_RULES = new HashSet<>();
    URL_RULES.add(new NoInLinking());
    URL_RULES.addAll(HostBlacklistFactory.getDefault());
  }
  public static final Set<SourceRule> SRC_RULES;
  static {
    SRC_RULES = new HashSet<>();
    SRC_RULES.add(new CosSimThreshold());
  }

  public static final int DEFAULT_DEPTH = 10;
  private Query<String, CompletableFuture<Source>> srcQuery;

  public AsyncQueryWebGraph(Source headVal, Query<String, CompletableFuture<Source>> srcQuery, String keywords) {
    this(headVal, srcQuery, keywords, DEFAULT_DEPTH);
  }

  public AsyncQueryWebGraph(Source headVal,
                            Query<String, CompletableFuture<Source>> srcQuery, String keywords, int depth) {
    super(headVal, depth);
    this.srcQuery = srcQuery;
    this.keywords = keywords;
    this.keySource = new DummySource("keywords dummy", keywords);
  }

  @Override
  public Set<Edge<Source, String>> getAllEdges(SourcedVertex<Source, String> rootVert) {
    Source rootSrc = rootVert.getVal();
    System.out.println("Starting search on: " + rootSrc.getURL());
    List<String> links = rootSrc.getLinks();

    // If we already have the sources, then just grab them
    Set<Edge<Source, String>> knownEs = links.stream()
        .filter(l -> this.loadedVertex(new DummySource(l)))
        .map(l -> {
          Vertex<Source, String> nv = this.getVertex(new DummySource(l));
          return new SourcedEdge<Source, String>(l, 0, rootVert, nv);
        }).collect(Collectors.toSet());

    // for the soures we don't have...
    Set<CompletableFuture<Source>> srcFs = links.stream()
        // if we haven't loaded yet
        .filter(l -> !this.loadedVertex(new WebSource(l, "")))
        // pass rules on url
        .filter(l -> URL_RULES.stream().allMatch(r ->
            r.verify(l, rootVert.getVal().getURL(), this)))
        // run asyncquery
        .map(l -> {
          try {
            return srcQuery.query(l);
          } catch (Exception e) {
            System.out.println("Async Graph send error: " + e.getMessage());
            CompletableFuture<Source> dud = new CompletableFuture<>();
            dud.complete(new DeadSource(l));
            return dud;
          }
        })
        // check to make sure Sources pass rules
        .map(srcFut ->
            srcFut.thenCompose(curSrc ->
                CompletableFuture.supplyAsync(
                    () -> {
                      if (curSrc instanceof DeadSource) {
                        return curSrc;
                      }
                      boolean viable = SRC_RULES.stream()
                          .allMatch(rule -> rule.verify(keySource, curSrc, this));
                      if (viable) {
                        return curSrc;
                      } else {
                        return NonViableSource.INSTANCE;
                      }
                    }
                )))
        .map(fut ->
            fut.exceptionally(ex -> {
              System.out.println("Async graph connection error: " + ex.getMessage());
              return NonViableSource.INSTANCE;
            })).collect(Collectors.toSet());
    // wait for all Source processing to finish
    CompletableFuture.allOf(srcFs.toArray(CompletableFuture[]::new)).join();
    // clear out all non-viable edges, make edges
    List<Edge<Source, String>> newEs = srcFs.stream()
        .map(CompletableFuture::join)
        .filter(s -> s != NonViableSource.INSTANCE)
        .map(s -> {
          Vertex<Source, String> nv = this.getVertex(s);
          System.out.println("adding..." + s.getURL());
          return new SourcedEdge<Source, String>(s.getURL(), 0, rootVert, nv);
        }).collect(Collectors.toList());
    // add to known edges and return
    knownEs.addAll(newEs);
    System.out.println("Search on " + rootSrc.getURL() + " complete.");
    return knownEs;
  }
}
