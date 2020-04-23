package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj;

import com.google.common.collect.Streams;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SimpleVertexGenerator extends Generator<SimpleVertex> {

  // Questions for tim
  // -Bias in graph construction? how to avoid it?
  // -Shrinking strategies? Takes a while the way I do it.
  // -How to ensure I've got good coverage...


  private static final int MAX_VERTS = 50;

  public SimpleVertexGenerator() {
    super(SimpleVertex.class);
  }

  @Override
  public SimpleVertex generate(
      SourceOfRandomness sourceOfRandomness,
      GenerationStatus generationStatus) {
    int numVerts = sourceOfRandomness.nextInt(1, MAX_VERTS);
    // instantiate new vertices
    List<SimpleVertex> verts = IntStream.rangeClosed(0, numVerts - 1)
        .mapToObj(n -> new SimpleVertex(String.valueOf(n)))
        .collect(Collectors.toList());

    // assign edges
    List<SimpleVertex> connectedVerts = verts.stream()
        .peek(v -> {    // generate a random list of edges to connect
          Set<Edge<String, Object>> edges =
              IntStream.generate(() -> sourceOfRandomness.nextInt(numVerts))
                  .limit(sourceOfRandomness.nextInt(numVerts)) // generate random list of neighbor vertices
                  .mapToObj(verts::get)
                  .map(nv -> new SimpleEdge(0, v, nv)) //instantiate random as edge destinations
                  .collect(Collectors.toSet());
          v.setEdges(edges);                          // attach to vertex
        }).collect(Collectors.toList());

    return connectedVerts.get(0);
  }

  @Override
  public List<SimpleVertex> doShrink(SourceOfRandomness random, SimpleVertex larger) {
    if (larger.getEdges().isEmpty())
      return Collections.emptyList();

    Stream<SimpleVertex> single = Stream.of(new SimpleVertex("0"));
    // set up stream of graphs from larger with one random edge removed

    Stream<SimpleVertex> reduced = Stream.generate(() -> {
      // copy vertex to prevent multiple changes to same graph
      SimpleVertex cpVert = (SimpleVertex) larger.clone();

      // dfs for all reachable nodes
      Set<SimpleVertex> vertSet = new HashSet<>();
      GraphUtils.dfs(cpVert, vertSet);
      List<SimpleVertex> vertList = new ArrayList<>(vertSet);
      List<SimpleVertex> pickList = new ArrayList<>(vertSet);

      // pick random node in the set and remove a random edge
      SimpleVertex cand;
      do{
        cand = pickList.get(random.nextInt(vertList.size()));
        pickList.remove(cand);
      } while (cand.getEdges().isEmpty());


      List<Edge<String, Object>> edges = new ArrayList<>(cand.getEdges());
      edges.remove(random.nextInt(edges.size()));
      cand.setEdges(new HashSet<>(edges));

      // return matching starting vertex
      return vertList.get(vertList.indexOf(larger));
    });

    // append stream and return next 50 results
    return Streams.concat(single, reduced).limit(50).collect(Collectors.toList());
  }
}
