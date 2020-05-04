package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;

import java.lang.reflect.Type;

public class VertexSerializer implements JsonSerializer<Vertex> {
  @Override
  public JsonElement serialize(Vertex vertex,
                               Type type,
                               JsonSerializationContext jsonSerializationContext) {
    return jsonSerializationContext.serialize(vertex.getVal());
  }
}
