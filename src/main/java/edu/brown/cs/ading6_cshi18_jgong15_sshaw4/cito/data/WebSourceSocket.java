package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

@WebSocket
public class WebSourceSocket {
  private static final Gson GSON = new Gson();
//  Gson gson = new GsonBuilder().registerTypeAdapter(WebSource.class,
//      new WebSourceAdapter()).create();
  private static final Queue<Session> SESSIONS = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;

  private static enum MESSAGE_TYPE {
    CONNECT,
    VERTEX
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    SESSIONS.add(session);
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
    assert received.get("type").getAsInt() == MESSAGE_TYPE.VERTEX.ordinal();

    JsonObject payload = received.get("payload").getAsJsonObject();

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.VERTEX.ordinal());
    JsonObject newPayload = new JsonObject();
    newPayload.add("id", payload.get("id"));

    String url = payload.get("url").getAsString();
    //Call algorithm here

    //dummy for testing
//    WebSource webSource = new WebSource("fillerurl.com", "fakehtml", new GregorianCalendar());
    newPayload.addProperty("url", url);

//    Board board = new Board(payload.get("board").getAsString());
//    Set<String> legal = board.play();
//    int score = 0;
//    for (String word : payload.get("text").getAsString().split(" ")) {
//      if (legal.contains(word)) {
//        score += Board.score(word);
//      }
//    }
//    newPayload.addProperty("score", score);

    toSend.add("payload", newPayload);

    String toSendStr = GSON.toJson(toSend);
    for (Session s : SESSIONS) {
      s.getRemote().sendString(toSendStr);
    }
  }
}
