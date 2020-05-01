package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.sockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.SourceSerializer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebSocket
public class WikiCitationSocket {
  private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Source.class,
      new SourceSerializer()).create();
  private static final HashMap<Integer, Session> SESSIONS = new HashMap();
  private static int nextId = 0;
  private static final int TIMELIMIT = 10;

  private static enum MESSAGE_TYPE {
    CONNECT,
    URLSUBMISSION,
    CITATION
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    SESSIONS.put(nextId, session);
    JsonObject message = new JsonObject();
    message.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    JsonObject payload = new JsonObject();
    payload.addProperty("id", nextId); // TODO: Why is ID a JSON Object and not Primitive?
    message.add("payload", payload);
    session.getRemote().sendString(GSON.toJson(message));
    nextId++;
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    SESSIONS.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    // Receiving a message, make sure the type is the one and only URLSUBMISSION
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.URLSUBMISSION.ordinal();

    // Unpack the payload
    JsonObject payload = received.get("payload").getAsJsonObject();
    JsonElement id = payload.get("id");

    // Process the payload
    String url = payload.get("url").getAsString();
    System.out.println("url: " + url);

//    // CHARLES CODE
//    Query<String, String> htmlQuery = new HTMLQuery(TIMELIMIT);
//    Query<String, Calendar> timeQuery = new TimeStampQuery(TIMELIMIT);
//    Set<Citation> citations = new HashSet<Citation>();
//    try {
//      String html = htmlQuery.query(url);
//      Calendar timestamp = timeQuery.query(url);
//      WikiHTMLParser parser = new WikiHTMLParser(url, html, timestamp);
//      citations = parser.parseForRawCitations();
//    } catch (QueryException e) {
//      e.printStackTrace();
//    }
//    System.out.println("citations: " + citations.size());
//    //pack the results

    // ANDREW's DUMMY PACKAGE
    // Preparing variables
    Citation citation1 = new Citation("Web", "#cite_note-7", "In 1948, humorist Paul Jennings coined the term resistentialism, a jocular play on resistance and existentialism, to describe \"seemingly spiteful behavior manifested by inanimate objects\",[5] where objects that cause problems (like lost keys or a runaway bouncy ball) are said to exhibit a high degree of malice toward humans.[6][7]", "https://www.nytimes.com/1948/06/13/archives/thingness-of-things-resistentialism-it-says-here-is-the-very-latest.html");
    List<Vertex<Source, String>> genVertices = citation1.getGenSources();
    List<Source> genSources = new ArrayList<Source>();
    for (Vertex<Source, String> vertex: genVertices) {
      if (vertex != null) {
        genSources.add(vertex.getVal());
      }
    }
    Type type = new TypeToken<List<Source>>(){}.getType(); // TODO: Wtf is this


    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.CITATION.ordinal());
    JsonObject newPayload = new JsonObject();
//    String citeTitle = citation1.getInitialWebSource().title();
//    String citeType = citation1.getSourceType();
//    String citeURL = citation1.getInitialWebSource().getURL();
//    Boolean hasCycles = citation1.getHasCycles();
    String jCiteSource = GSON.toJson(citation1.getInitialWebSource(), Source.class); // title, type, url, cycles
    String jGenSources = GSON.toJson(genSources, type); // title, url

    newPayload.add("id", id);



    // JSON the citation here
    Type type = new TypeToken<List<Source>>(){}.getType(); // TODO: Wtf is this
    for (Citation citation: citations) {
      JsonObject toSend = new JsonObject();
      toSend.addProperty("type", MESSAGE_TYPE.CITATION.ordinal());
      JsonObject newPayload = new JsonObject();

      List<Vertex<Source, String>> genVertices = citation.getGenSources();
      List<Source> genSources = new ArrayList<Source>();
      for (Vertex<Source, String> vertex: genVertices) {
        if (vertex != null) {
          genSources.add(vertex.getVal()); // Source objects
        }
      }

      // Preparing payload
      System.out.println("sources: " + genSources.size());
      String jSource = GSON.toJson(genSources, type);
      String citeSource = GSON.toJson(citation.getInitialWebSource(), Source.class);
      Boolean hasCycles = citation.getHasCycles();
      newPayload.add("id", id);
      newPayload.addProperty("citeSource", citeSource);
      newPayload.addProperty("genSources", jSource);
      newPayload.addProperty("hasCycles", hasCycles);

      // Sending the payload
      toSend.add("payload", newPayload);
      String toSendStr = GSON.toJson(toSend);
      System.out.println("tosend: " + toSendStr);
      SESSIONS.get(id.getAsInt()).getRemote().sendString(toSendStr);
    }
    */


  }
}
