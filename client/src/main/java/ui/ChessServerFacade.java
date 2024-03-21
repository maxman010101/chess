package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import models.Auth;
import models.Game;
import models.User;
import requests.JoinGameRequest;
import requests.LogOutRequest;
import responses.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.InputStream;
public class ChessServerFacade {
    private final String serverUrl;
    private String authToken;
    private String username;
    public ChessServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    public GameListResponse listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, GameListResponse.class, authToken);
    }
    public Auth register(String name, String pass, String email) throws ResponseException {
        var path = "/user";
        var response = this.makeRequest("POST", path, new User(name, pass, email), Auth.class, null);
        authToken = response.authToken;
        username = response.username;
        return response;
    }
    public Auth login(String name, String pass) throws ResponseException {
        var path = "/session";
        var response = this.makeRequest("POST", path, new User(name, pass, null), Auth.class, null);
        authToken = response.authToken;
        username = response.username;
        return response;
    }
    public JoinGameResponse joinGame(int gameID, ChessGame.TeamColor color, Game game, String name) throws ResponseException{
        var path = "/game";
        if(color == ChessGame.TeamColor.WHITE){
            game.setWhiteUsername(username);
            return this.makeRequest("PUT", path, new JoinGameRequest(color, gameID), JoinGameResponse.class, authToken);
        }
        if(color == ChessGame.TeamColor.BLACK){
            game.setBlackUsername(username);
            return this.makeRequest("PUT", path, new JoinGameRequest(color, gameID), JoinGameResponse.class, authToken);
        }
        if(color == null){
            return this.makeRequest("PUT", path, new JoinGameRequest(color, gameID), JoinGameResponse.class, authToken);
        }
        else
            return null;
    }
    public CreateGameResponse createGame(String name) throws ResponseException {
        var path = "/game";
        var response = this.makeRequest("POST", path, new Game(1, name, null, null, new ChessGame()), CreateGameResponse.class, authToken);
        //Game curGame = response.
        return response;
    }
    public void logOut() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, LogOutResponse.class, authToken);
    }
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String token) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("Authorization", token);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage(), 500);
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
            throw new ResponseException("failure: " + status, status);
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
}
