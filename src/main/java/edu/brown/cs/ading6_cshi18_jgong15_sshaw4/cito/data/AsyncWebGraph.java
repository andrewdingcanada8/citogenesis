package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.blacklist.HostBlacklistFactory;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.blacklist.NoInLinking;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.blacklist.Rule;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Deadend;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedEdge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.SourcedVertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class AsyncWebGraph extends RootedSourcedMemGraph<Source, String> {
  public static final Set<Rule> RULES;
  static {
    RULES = new HashSet<>();
    RULES.add(new NoInLinking());
    RULES.addAll(HostBlacklistFactory.getDefault());
  }

  public static final int DEFAULT_DEPTH = 10;
  private Query<String, CompletableFuture<Source>> srcQuery;

  public AsyncWebGraph(Source headVal, Query<String, CompletableFuture<Source>> srcQuery) {
    this(headVal, srcQuery, DEFAULT_DEPTH);
  }

  public AsyncWebGraph(Source headVal, Query<String, CompletableFuture<Source>> srcQuery, int depth) {
    super(headVal, depth);
    this.srcQuery = srcQuery;
  }


  @Override
  public Set<Edge<Source, String>> getAllEdges(SourcedVertex<Source, String> vert)
      throws GraphException {
    Source src = vert.getVal();
    System.out.println("Starting search on: " + src.getURL());
    List<String> links = src.getLinks();

    // If we already have the sources, then just grab them
    Set<Edge<Source, String>> knownEs = links.stream()
        .filter(l -> this.loadedVertex(new WebSource(l, "", null)))
        .map(l -> {
          Vertex<Source, String> nv = this.getVertex(new WebSource(l, "", null));
          return new SourcedEdge<Source, String>(l, 0, vert, nv);
        }).collect(Collectors.toSet());

    // for the soures we don't have...
    Set<CompletableFuture<? extends Edge<Source, String>>> edgeFs = links.stream()
        // if we haven't loaded yet
        .filter(l -> !this.loadedVertex(new WebSource(l, "", null)))
        // pass rules on url
        .filter(l -> RULES.stream().allMatch(r -> r.verify(l, vert, this)))
        // run asyncquery
        .limit(10) // test
        .map(l -> {
          try {
            return srcQuery.query(l);
          } catch (Exception e) {
            System.out.println("Async Graph send error: " + e.getMessage());
            CompletableFuture<Source> dud = new CompletableFuture<>();
            dud.complete(new DeadSource());
            return dud;
          }
        })
        // when done, obtain new vertex and make an edge
        .map(srcFuture ->
            srcFuture.thenCompose(s ->
                CompletableFuture.supplyAsync(() -> {
                  Vertex nv = this.getVertex(s);
                  System.out.println("adding.... " + s.getURL());
                  return new SourcedEdge<Source, String>(src.getURL(), 0, vert, nv);
                })))
        // if we hit a QueryException/any others, just attach a dead source instead
        .map(eFut ->
            eFut.exceptionally(ex -> {
              System.out.println("Async Graph connection error: " + ex.getMessage());
              return new SourcedEdge<Source, String>(
                  "dead", 0, vert, new Deadend<>(new DeadSource()));
            }))
        .collect(Collectors.toSet());
    // wait for all requests to finish
    try {
      CompletableFuture.allOf(edgeFs.toArray(CompletableFuture[]::new)).get(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException | TimeoutException e) {
      System.out.println("Link search on " + src.getURL() + " errored: " + e.getMessage());
    }

    knownEs.addAll(edgeFs.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    System.out.println("Search on " + src.getURL() + " complete.");
    return knownEs;
  }
}
