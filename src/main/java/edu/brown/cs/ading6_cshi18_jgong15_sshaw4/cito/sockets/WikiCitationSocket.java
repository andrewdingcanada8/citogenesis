package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.sockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.SourceSerializer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Wiki;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers.WikiHTMLParser;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.WikiQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.TimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    payload.addProperty("id", nextId);
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
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.URLSUBMISSION.ordinal();

    //unpack the payload
    JsonObject payload = received.get("payload").getAsJsonObject();
    JsonElement id = payload.get("id");

    //process the payload
    String url = payload.get("url").getAsString();
    System.out.println("url: " + url);
    Set<Citation> citations = new HashSet<>();
    try {
      Wiki wiki = new WikiQuery(TIMELIMIT).query(url);
      citations = wiki.getCitationSet();
    } catch (QueryException e) {
      e.printStackTrace();
    }
    System.out.println("citations: " + citations.size());
    //pack the results

    //JSON the citation here
    Type type = new TypeToken<List<Source>>(){}.getType();
    for (Citation citation: citations) {
      JsonObject toSend = new JsonObject();
      toSend.addProperty("type", MESSAGE_TYPE.CITATION.ordinal());
      JsonObject newPayload = new JsonObject();
      newPayload.add("id", id);

      List<Vertex<Source, String>> genVertices = citation.getGenSources();
      List<Source> genSources = new ArrayList<Source>();
      for (Vertex<Source, String> vertex: genVertices) {
        if (vertex != null) {
          genSources.add(vertex.getVal());
        }
      }
      System.out.println("sources: " + genSources.size());
      String jSource = GSON.toJson(genSources, type);
      String citeSource = GSON.toJson(citation.getInitialWebSource(), Source.class);
      newPayload.addProperty("citeSource", citeSource);
      newPayload.addProperty("genSources", jSource);
      Boolean hasCycles = citation.getHasCycles();
      newPayload.addProperty("hasCycles", hasCycles);

      System.out.println(newPayload);
      toSend.add("payload", newPayload);

      String toSendStr = GSON.toJson(toSend);
      System.out.println("tosend: " + toSendStr);
      SESSIONS.get(id.getAsInt()).getRemote().sendString(toSendStr);
    }
  }
}
