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


  private static final int MAX_VERTS = 25;

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
}
