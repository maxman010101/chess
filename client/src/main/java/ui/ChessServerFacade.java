package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import models.Auth;
import models.Game;
import requests.*;
import responses.*;
import services.ClearService;
import services.GameServices;
import services.UserServices;

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
    private UserServices userServices = new UserServices();
    private GameServices gameServices = new GameServices();
    private ClearService clearServices = new ClearService();

    public ChessServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    public GameListResponse listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, GameListResponse.class);
    }
    public RegisterResponse register(String name, String pass, String email) throws ResponseException, responses.ResponseException, DataAccessException {
        var path = "/user";
        var response = this.makeRequest("POST", path, userServices.register(name, pass, email), RegisterResponse.class);
        authToken = response.auth().authToken;
        return response;
    }
    public LoginResponse login(String name, String pass) throws ResponseException, responses.ResponseException, DataAccessException {
        var path = "/session";
        var response = this.makeRequest("POST", path, userServices.login(name, pass), LoginResponse.class);
        authToken = response.auth().authToken;
        return response;
    }
    public JoinGameResponse joinGame(int gameID, ChessGame.TeamColor color) throws ResponseException, responses.ResponseException, DataAccessException {
        var path = "/game";
        return this.makeRequest("PUT", path, gameServices.joinGame(authToken, color, gameID), JoinGameResponse.class);
    }
    public CreateGameResponse createGame(String name) throws ResponseException, responses.ResponseException, DataAccessException {
        var path = "/game";
        return this.makeRequest("POST", path, gameServices.createGame(authToken, name), CreateGameResponse.class);
    }
    public LogOutResponse logOut() throws ResponseException, responses.ResponseException, DataAccessException {
        var path = "/session";
        return this.makeRequest("DELETE", path, userServices.logout(authToken), LogOutResponse.class);
    }
    public ClearResponse clearData() throws ResponseException, responses.ResponseException, DataAccessException {
        var path = "/db";
        return this.makeRequest("DELETE", path, clearServices.clearData(), ClearResponse.class);
    }
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
