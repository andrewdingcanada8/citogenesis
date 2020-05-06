package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.Main;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source.DeadSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source.DummySource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.source.NonViableSource;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source.CosSimThreshold;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.source.SourceRule;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.url.HostBlacklistFactory;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.url.URLRule;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops.CosSim;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.AsyncRootedSourcedMemGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncSearchWebGraph extends AsyncRootedSourcedMemGraph<Source, String> {
  public static final int DEFAULT_DEPTH = 10;
  public static final double DEFAULT_THRESH = 0.2;
  private Query<String, CompletableFuture<Source>> srcQuery;
  private Source keySource;
  private double relThresh;

  public AsyncSearchWebGraph(Source headVal,
                             Query<String, CompletableFuture<Source>> srcQuery,
                             String keyText) {
    this(headVal, srcQuery, keyText, DEFAULT_DEPTH);
  }

  public AsyncSearchWebGraph(Source headVal,
                             Query<String, CompletableFuture<Source>> srcQuery,
                             String keyText,
                             int depth) {
    this(headVal, srcQuery, keyText, depth, DEFAULT_THRESH);
  }

  public AsyncSearchWebGraph(Source headVal,
                             Query<String, CompletableFuture<Source>> srcQuery,
                             String keyText,
                             int depth,
                             double relThresh) {
    super(headVal, depth);
    this.srcQuery = srcQuery;
    this.keySource = new DummySource("keywords dummy", keyText);

    if (relThresh < 0 || relThresh > 1.0) {
      throw new IllegalArgumentException("relThresh must be between [0, 1.0] (inclusive)");
    }

    this.relThresh = relThresh;
    SRC_RULES.add((src, prevSrc, graph)
        -> new CosSim().apply(src.getContent(), prevSrc.getContent()) >= relThresh);
  }

  @Override
  public Set<Edge<Source, String>> getAllEdges(SourcedVertex<Source, String> rootVert) {
    Source rootSrc = rootVert.getVal();

    if (rootSrc instanceof DeadSource) {
      return Collections.emptySet();
    }

    if (Main.isVerbose()) {
      System.err.println("Starting search on: " + rootSrc.getURL());
    }
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
        .filter(l -> !this.loadedVertex(new DummySource(l)))
        // pass rules on url
        .filter(l -> URL_RULES.stream().allMatch(r ->
            r.verify(l, rootVert.getVal().getURL(), this)))
        // run asyncquery
        .map(l -> {

          if (Main.isVerbose()) {
            System.err.println("processing " + l + ".");
          }

          try {
            return srcQuery.query(l);
          } catch (Exception e) {
            System.err.println("Async Graph send error: " + e.getMessage());
            CompletableFuture<Source> dud = new CompletableFuture<>();
            dud.complete(NonViableSource.INSTANCE);
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
              System.err.println("Async graph connection error: " + ex.getMessage());
              return NonViableSource.INSTANCE;
            })).collect(Collectors.toSet());
    // wait for all Source processing to finish
    CompletableFuture.allOf(srcFs.toArray(CompletableFuture[]::new)).join();
    // clear out all non-viable edges, make edges
    List<Edge<Source, String>> newEs = srcFs.stream()
        .map(CompletableFuture::join)
        .filter(s -> !s.equals(NonViableSource.INSTANCE))
        .map(s -> {
          Vertex<Source, String> nv = this.getVertex(s);

          if (Main.isVerbose()) {
            System.err.println("adding..." + s.getURL());
          }
          return new SourcedEdge<>(s.getURL(), 0, rootVert, nv);
        }).collect(Collectors.toList());
    // add to known edges and return
    knownEs.addAll(newEs);

    if (Main.isVerbose()) {
      System.err.println("Search on " + rootSrc.getURL() + " complete.");
    }

    return knownEs;
  }

  public static final Set<URLRule> URL_RULES;

  static {
    URL_RULES = new HashSet<>();
    //URL_RULES.add(new NoInLinking());
    URL_RULES.add((url, prevUrl, graph) -> !prevUrl.equals("dead"));
    URL_RULES.addAll(HostBlacklistFactory.getDefault());
  }

  public static final Set<SourceRule> SRC_RULES;

  static {
    SRC_RULES = new HashSet<>();
  }
}
