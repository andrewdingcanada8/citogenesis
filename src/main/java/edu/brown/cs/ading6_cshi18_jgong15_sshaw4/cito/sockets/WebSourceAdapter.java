//package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;
//
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParseException;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;
//import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
//
//import java.lang.reflect.Type;
//import java.util.List;
//
//public class WebSourceAdapter implements JsonSerializer<WebSource>, JsonDeserializer<WebSource> {
//  @Override
//  public JsonElement serialize(Vertex<WebSource, String> source, Type typeOfSrc,
//                               JsonSerializationContext context) {
//    JsonObject obj = new JsonObject();
//    String sourceURL = source.getVal().getURL();
//    List<String> sourceLinks = source.getLinks();
//    String linkString = sourceLinks.get(0);
//    for (String link : sourceLinks.subList(1, sourceLinks.size())) {
//      linkString = linkString + ";" + link;
//    }
//    //Properties here
//    obj.addProperty("vertexURL", sourceURL);
//    obj.addProperty("vertexLinks", linkString);
//
//    return obj;
//  }
//
//  @Override
//  public WebSource deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//    return null;
//  }
//}
