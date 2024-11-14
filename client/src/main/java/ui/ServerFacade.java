package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import gamerequesthelper.JoinGameRequest;
import model.AuthData;
import model.GameData;
import model.UserData;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class ServerFacade {
  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }

  public AuthData register(UserData user) throws ResponseException {
    var path = "/user";
    return this.makeRequest("POST", path, user, AuthData.class,null);
  }

  public AuthData login(UserData user) throws ResponseException {
    var path = "/session";
    return this.makeRequest("POST", path, user, AuthData.class,null);
  }
  public void logout(AuthData authData) throws ResponseException {
    var path = "/session";
    this.makeRequest("DELETE", path, null, null,authData);
  }

  public GameData createGame(String gameName, AuthData authData) throws ResponseException {
    var path = "/game";
    return this.makeRequest("POST", path, authData, GameData.class,null);
  }

  public ArrayList<GameData> listGames(AuthData authData) throws ResponseException {
    var path = "/game";
    record ListGamesResponse(Collection<GameData> games) {}
    var response = this.makeRequest("GET", path, null, ListGamesResponse.class, authData);
    return new ArrayList<>(response.games);

  }

  public ChessGame joinGame(String playerColor, int gameId, AuthData authData) throws ResponseException {
    var path = "/game";
    JoinGameRequest request = new JoinGameRequest(playerColor,gameId);
    return this.makeRequest("PUT", path, request, ChessGame.class, authData);
  }


  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, AuthData authData) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      if (authData != null && authData.authToken() != null) {
        http.setRequestProperty("Authorization", authData.authToken());
      }

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }


  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException(status, "failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }


  //inner class created below

  public class ResponseException extends Exception {
    private final int statusCode;
    public ResponseException(int statusCode, String message) {
      super(message);
      this.statusCode = statusCode;
    }
  }
 //end of server facade
}
