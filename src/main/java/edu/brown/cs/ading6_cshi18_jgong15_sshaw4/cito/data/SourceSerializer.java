package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SourceSerializer implements JsonSerializer<Source> {
  @Override
  public JsonElement serialize(Source source,
                               Type type,
                               JsonSerializationContext jsonSerializationContext) {
    JsonObject jsonSrc = new JsonObject();
    jsonSrc.addProperty("title", source.title());
    jsonSrc.addProperty("url", source.getURL());
    return jsonSrc;
  }
}
