package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
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
    Map<Vertex, Set<Vertex>> adjacencyMap = new HashMap();
    for (Vertex vertex : vertices) {
      Set<Vertex> neighborSet = new HashSet<>();
      try {
        Set<Edge> edges = vertex.getEdges();
        for (Edge edge : edges) {
          neighborSet.add(edge.getDest());
        }
      } catch (GraphException e) {
        adjacencyMap.put(vertex, neighborSet);
        continue;
      }
      adjacencyMap.put(vertex, neighborSet);
    }

    JsonElement vertexArray = jsonSerializationContext.serialize(vertices);
    JsonElement vertexMap = jsonSerializationContext.serialize(adjacencyMap);
    jsonSrc.add("vertices", vertexArray);
    jsonSrc.add("map", vertexMap);
    return jsonSrc;
  }
}
