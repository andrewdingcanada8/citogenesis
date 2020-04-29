package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.sockets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Citation;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers.WikiHTMLParser;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.TimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@WebSocket
public class WikiCitationSocket {
  private static final Gson GSON = new Gson();
  private static final HashMap<Integer, Session> SESSIONS = new HashMap();
  private static int nextId = 0;
  private static final int TIMELIMIT = 10;

  private static enum MESSAGE_TYPE {
    CONNECT,
    URLSUBMISSION,
    CITATIONS
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
    Query<String, String> htmlQuery = new HTMLQuery(TIMELIMIT);
    Query<String, Calendar> timeQuery = new TimeStampQuery(TIMELIMIT);
    Set<Citation> citations = new HashSet<Citation>();
    try {
      String html = htmlQuery.query(url);
      Calendar timestamp = timeQuery.query(url);
      WikiHTMLParser parser = new WikiHTMLParser(url, html, timestamp);
      citations = parser.parseForRawCitations();
    } catch (QueryException e) {
      e.printStackTrace();
    }


    //pack the results
    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.CITATIONS.ordinal());
    JsonObject newPayload = new JsonObject();
    newPayload.add("id", id);

    //parse the citation here
    newPayload.add("citations", citations);

    toSend.add("payload", newPayload);

    String toSendStr = GSON.toJson(toSend);
    SESSIONS.get(id.getAsInt()).getRemote().sendString(toSendStr);
  }
}
