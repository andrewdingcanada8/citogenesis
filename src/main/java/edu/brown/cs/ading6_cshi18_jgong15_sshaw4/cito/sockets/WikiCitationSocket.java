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
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.WikiQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
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
import java.util.*;

@WebSocket
public class WikiCitationSocket {
  private static final Gson GSON = new GsonBuilder()
          .registerTypeAdapter(Source.class, new SourceSerializer())
          .registerTypeAdapter(RootedSourcedMemGraph.class, new GraphSerializer())
          .registerTypeAdapter(Vertex.class, new VertexSerializer())
          .create();
  private static final HashMap<Integer, Session> SESSIONS = new HashMap();
  private static int nextId = 0;
  private static final int TIME_LIMIT = 10;

  private static enum MESSAGE_TYPE {
    CONNECT,
    URLSUBMISSION,
    HTML,
    CITATION
  }

  /**
   * When WebSocket first connects, this method messages the client to let it know of the
   * connection, and supplies a Session ID.
   * @param session - Session Object
   * @throws IOException - IOException
   */
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
    System.out.println("[Server] INFO: Socket closed due to: " + reason);
    SESSIONS.remove(session);
  }

  /**
   * OnWebSocketMessage is called whenever a new WebSocket message is sent from the client to the
   * server. After the server sends a CONNECT message, the client sends back a URLSUBMISSION, which
   * can be used to initiate the Wiki object. This class will then sequentially send messages to the
   * client with information for the page like HTML, citation data etc.
   * @param session - Session object
   * @param message - JSON String that contains an MESSAGE_TYPE 'type' and JsonObject 'payload' in
   *                its top level
   * @throws IOException - IOException
   */
  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.URLSUBMISSION.ordinal();

    // Extract message payload
    JsonObject payload = received.get("payload").getAsJsonObject();
    JsonElement id = payload.get("id");

    // Creating Wiki
    Wiki wiki = null;
    String url = payload.get("url").getAsString();
    System.out.println("[SERVER] Recieved URL: " + url); // TODO: Delete Later
    String html = "";
    try {
      wiki = new WikiQuery(TIME_LIMIT).query(url);
      html = wiki.getContentHTML();
    } catch (QueryException e) {
      e.printStackTrace();
    }
    // If the program exits here, the wiki has failed to be constructed.
    assert wiki != null;
    // If the program exits here, wiki was not able to access webpage correctly
    assert !html.equals("");

    // Preparing HTML payload
    JsonObject htmlPayload = new JsonObject();
    htmlPayload.add("id", id);
    htmlPayload.addProperty("html", html);

    // Preparing HTML message
    JsonObject htmlToSend = new JsonObject();
    htmlToSend.addProperty("type", MESSAGE_TYPE.HTML.ordinal());

    // Sending HTML message
    htmlToSend.add("payload", htmlPayload);
    String htmlToSendStr = GSON.toJson(htmlToSend);
    System.out.println("tosend: " + htmlToSendStr); // TODO: Delete Later
    SESSIONS.get(id.getAsInt()).getRemote().sendString(htmlToSendStr);


    // Getting Citation ID
    Set<String> citationIDs = new HashSet<>();
    citationIDs = wiki.getCitationIDs();
    Type type = new TypeToken<List<Source>>() { }.getType();

    // Sequentially building and sending citation information to client
    for (String citationID: citationIDs) {
      // Building Citation
      Citation citation = wiki.getCitationFromID(citationID, 60, 3, 0.2);
      List<Vertex<Source, String>> genVertices = citation.getGenSources();
      List<Source> genSources = new ArrayList<Source>();
      for (Vertex<Source, String> vertex: genVertices) {
        if (vertex != null) {
          genSources.add(vertex.getVal());
        }
      }
      Graph<Source, String> graph = citation.getGraph();

      // Declaring fields in payload
      String citeRefText;
      String citeType;
      String citeId;
      Boolean hasCycles;
      String citeTitle;
      String citeURL;
      JsonElement jGenSources;
      JsonElement jGraph;
      // Filling fields in payload
      citeRefText = citation.getReferenceText();
      citeType = citation.getType();
      citeId = citation.getId();
      hasCycles = citation.getHasCycles();
      if ((citeType.equals("Web")) && citation.getInitialWebSource() != null) {
        citeTitle = citation.getInitialWebSource().title();
        citeURL = citation.getInitialWebSource().getURL();
        jGenSources = GSON.toJsonTree(genSources, type); // title, url
        jGraph = GSON.toJsonTree(graph, type); // vertices, map
      } else {
        citeTitle = "";
        citeURL = "";
        jGenSources = null;
        jGraph = null;
      }
      // Prepare Payload, append fields
      JsonObject citePayload = new JsonObject();
      citePayload.add("id", id);
      citePayload.addProperty("citeRefText", citeRefText);
      citePayload.addProperty("citeId", citeId);
      citePayload.addProperty("citeTitle", citeTitle);
      citePayload.addProperty("citeType", citeType);
      citePayload.addProperty("citeURL", citeURL);
      citePayload.addProperty("hasCycles", hasCycles);
      citePayload.add("jGenSources", jGenSources);
      citePayload.add("jGraph", jGraph);

      // Preparing ToSend object with payload and message type inside
      JsonObject citeToSend = new JsonObject();
      citeToSend.addProperty("type", MESSAGE_TYPE.CITATION.ordinal());
      citeToSend.add("payload", citePayload);
      // Sent ToSend to client
      String citeToSendStr = GSON.toJson(citeToSend);
      System.out.println("tosend: " + citeToSendStr); // TODO: Delete Later
      SESSIONS.get(id.getAsInt()).getRemote().sendString(citeToSendStr);
    }
  }
}
