package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleVertexGenerator extends Generator<Vertex> {

  private static final int MAX_VERTS = 100;

  public SimpleVertexGenerator() {
    super(Vertex.class);
  }

  @Override
  public Vertex<String, Object> generate(
      SourceOfRandomness sourceOfRandomness,
      GenerationStatus generationStatus) {
    int numVerts = sourceOfRandomness.nextInt(1, MAX_VERTS);
    // instantiate new vertices
    List<SimpleVertex> verts = IntStream.rangeClosed(0, numVerts - 1)
        .mapToObj(n -> new SimpleVertex(String.valueOf(n)))
        .collect(Collectors.toList());

    // assign edges
    List<SimpleVertex> connectedVerts = verts.stream()
        .peek(v -> {
          // generate a random list of edges to connect
          Set<Edge<String, Object>> neighbors =
              IntStream.generate(() -> sourceOfRandomness.nextInt(numVerts))
              .limit(sourceOfRandomness.nextInt(numVerts)) // generate random list of neighbor vertices
              .mapToObj(verts::get)
              .map(nv -> new SimpleEdge(0, v, nv)) //instantiate random as edge destinations
              .collect(Collectors.toSet());
          v.setEdges(neighbors);                          // attach to vertex
        }).collect(Collectors.toList());

    return connectedVerts.get(0);
  }
}
