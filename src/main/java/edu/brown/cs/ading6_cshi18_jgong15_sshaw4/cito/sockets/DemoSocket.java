package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.sockets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

@WebSocket
public class DemoSocket {
  private static final Gson GSON = new Gson();
  private static final HashMap<Integer, Session> SESSIONS = new HashMap();
  private static int nextId = 0;

  private static enum MESSAGE_TYPE {
    CONNECT,
    VERTEX,
    NEIGHBORS
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
    assert received.get("type").getAsInt() == MESSAGE_TYPE.VERTEX.ordinal();

    JsonObject payload = received.get("payload").getAsJsonObject();
    JsonElement id = payload.get("id");

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.NEIGHBORS.ordinal());
    JsonObject newPayload = new JsonObject();
    newPayload.add("id", id);

    JsonElement url = payload.get("url");
    System.out.println(payload);

    //Call algorithm here
    newPayload.add("url", url);

    toSend.add("payload", newPayload);

    String toSendStr = GSON.toJson(toSend);
    SESSIONS.get(id.getAsInt()).getRemote().sendString(toSendStr);
  }
}
