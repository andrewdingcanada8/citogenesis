package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.obj;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// CURRENTLY BROKEN
public class StronglyConnectedSimpleGenerator extends Generator<SimpleVertex> {

  static final int MAX_VERTS = 20;

  public StronglyConnectedSimpleGenerator() {
    super(SimpleVertex.class);
  }

  @Override
  public SimpleVertex generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
    int numVerts = sourceOfRandomness.nextInt(MAX_VERTS) + 1;

    // generate vertices
    List<SimpleVertex> verts = IntStream.range(0, numVerts)               //min is going to be set to 0
        .mapToObj(n -> {
          SimpleVertex v = new SimpleVertex(String.valueOf(sourceOfRandomness.nextInt(1, numVerts + 1)));
          v.setEdges(new HashSet<>());
          return v;
        }).collect(Collectors.toList());

    // construct random tree
    for (int i = 1; i < verts.size(); i++) {
      int inTree = sourceOfRandomness.nextInt(i);
      verts.get(inTree).getEdges().add(new SimpleEdge(0, verts.get(inTree), verts.get(i)));
    }

    // construct back edges for completeness
    // first, pull out root (at index 0) and shuffle the rest
    SimpleVertex root = verts.remove(0);
    Collections.shuffle(verts);

    // set root as the first one and add back edges as needed
    verts.add(0, root);
    for (int i = 1; i < verts.size(); i++) {
      int inStrong = sourceOfRandomness.nextInt(i);
      verts.get(i).getEdges().add(new SimpleEdge(0, verts.get(i), verts.get(inStrong)));
    }

    // choose min node
    SimpleVertex min = verts.remove(sourceOfRandomness.nextInt(numVerts));
    min.setVal("0");
    AtomicInteger extraIndex = new AtomicInteger(numVerts + 1);
    verts.stream().forEach(v -> v.getEdges().addAll(
        IntStream
            .range(0, sourceOfRandomness.nextInt(3))
            .mapToObj(n -> new SimpleEdge(0.0,
                v,
                new SimpleVertex(String.valueOf(extraIndex.incrementAndGet())))).collect(Collectors.toSet())));
    // add back in and return random starting vertex
    verts.add(min);
    return verts.get(sourceOfRandomness.nextInt(numVerts));
  }
}
