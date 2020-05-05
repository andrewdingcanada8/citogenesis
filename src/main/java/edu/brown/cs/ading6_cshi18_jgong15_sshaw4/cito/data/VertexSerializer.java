package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;

import java.lang.reflect.Type;
import java.util.Collection;

public class VertexSerializer implements JsonSerializer<Vertex> {
  @Override
  public JsonElement serialize(Vertex vertex,
                               Type type,
                               JsonSerializationContext jsonSerializationContext) {
//    vertex.getVal().
//    Type vertexType = new TypeToken<>() { }.getType();
    return jsonSerializationContext.serialize(vertex.getVal(), Source.class);
  }
}
