package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphSerializer implements JsonSerializer<RootedSourcedMemGraph> {
  @Override
  public JsonElement serialize(RootedSourcedMemGraph graph,
                               Type type,
                               JsonSerializationContext jsonSerializationContext) {
    JsonObject jsonSrc = new JsonObject();
    Collection<Vertex> vertices = graph.getLoadedVertices();
//    JsonElement vertexArray = jsonSerializationContext.serialize(vertices);
//    Map<Vertex, Set<Vertex>> adjacencyMap = new HashMap();
    JsonObject adjacencyMap = new JsonObject();

    Type neighborSetType = new TypeToken<Set<Vertex>>() { }.getType();
    for (Vertex vertex : vertices) {
      Set<Vertex> neighborSet = new HashSet<>();
      try {
        Set<Edge> edges = vertex.getEdges();
        for (Edge edge : edges) {
          neighborSet.add(edge.getDest());
        }
      } catch (GraphException e) {
        adjacencyMap.add(vertex.getVal().toString(),
            jsonSerializationContext.serialize(neighborSet, neighborSetType));
        continue;
      }
      adjacencyMap.add(vertex.getVal().toString(),
          jsonSerializationContext.serialize(neighborSet, neighborSetType));
    }
    Type verticesType = new TypeToken<Collection<Vertex>>() { }.getType();
    Type adjMType = new TypeToken<Map<Vertex, Set<Vertex>>>() { }.getType();
    JsonElement vertexArray = jsonSerializationContext.serialize(vertices, verticesType);
//    JsonElement vertexMap = jsonSerializationContext.serialize(adjacencyMap, adjMType);
    jsonSrc.add("vertices", vertexArray);
    jsonSrc.add("map", adjacencyMap);
    return jsonSrc;
  }
}
