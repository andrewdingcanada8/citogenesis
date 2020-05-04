package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.sockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.GraphSerializer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.SourceSerializer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.VertexSerializer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Wiki;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers.WikiHTMLParser;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.WikiQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.TimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Graph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebSocket
public class GraphSocket {
  private static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(Source.class, new SourceSerializer())
      .registerTypeAdapter(RootedSourcedMemGraph.class, new GraphSerializer())
      .registerTypeAdapter(Vertex.class, new VertexSerializer())
      .create();
  private static final HashMap<Integer, Session> SESSIONS = new HashMap();
  private static int nextId = 0;
  private static final int TIMELIMIT = 10;

  private static enum MESSAGE_TYPE {
    CONNECT,
    URLSUBMISSION,
    GRAPH
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

    Wiki wiki = null;
    System.out.println("[SERVER] Recieved URL: " + url); // TODO: Delete Later
    String html = "";
    try {
      wiki = new WikiQuery(TIMELIMIT).query(url);
      html = wiki.getContentHTML();
    } catch (QueryException e) {
      e.printStackTrace();
    }
    // If the program exits here, the wiki has failed to be constructed.
    assert wiki != null;
    // If the program exits here, wiki was not able to access webpage correctly
    assert !html.equals("");

    Set<String> citationIDs = new HashSet<>();
    citationIDs = wiki.getCitationIDs();
    Type type = new TypeToken<List<Source>>() { }.getType();

    for (String citationID: citationIDs) {
      // Building Citation
      Citation citation = wiki.getCitationFromID(citationID);
      Graph<Source, String> graph = citation.getGraph();

      // Declaring fields in payload
      String citeRefText;
      String citeType;
      String citeId;
      Boolean hasCycles;
      String citeTitle;
      String citeURL;
      JsonElement jGraph;
      // Filling fields in payload
      citeRefText = citation.getReferenceText();
      citeType = citation.getSourceType();
      citeId = citation.getId();
      hasCycles = citation.getHasCycles();
      if ((citeType.equals("Web")) && citation.getInitialWebSource() != null) {
        citeTitle = citation.getInitialWebSource().title();
        citeURL = citation.getInitialWebSource().getURL();
        jGraph = GSON.toJsonTree(graph, type);
      } else {
        citeTitle = "";
        citeURL = "";
        jGraph = null;
      }
      // Prepare Payload, append fields
      JsonObject graphPayload = new JsonObject();
      graphPayload.add("id", id);
      graphPayload.addProperty("citeRefText", citeRefText);
      graphPayload.addProperty("citeId", citeId);
      graphPayload.addProperty("citeTitle", citeTitle);
      graphPayload.addProperty("citeType", citeType);
      graphPayload.addProperty("citeURL", citeURL);
      graphPayload.addProperty("hasCycles", hasCycles);
      graphPayload.add("jGraph", jGraph);

      JsonObject graphToSend = new JsonObject();
      graphToSend.addProperty("type", GraphSocket.MESSAGE_TYPE.GRAPH.ordinal());
      graphToSend.add("payload", graphPayload);
      // Sent ToSend to client
      String citeToSendStr = GSON.toJson(graphToSend);
      System.out.println("tosend: " + citeToSendStr); // TODO: Delete Later
      SESSIONS.get(id.getAsInt()).getRemote().sendString(citeToSendStr);
    }
  }
}
