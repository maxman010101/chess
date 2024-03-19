package ui;

import com.google.gson.Gson;
import models.Auth;
import models.Game;
import responses.ClearResponse;
import responses.CreateGameResponse;
import responses.JoinGameResponse;
import responses.LogOutResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.InputStream;

public class ChessServerFacade {
    private final String serverUrl;

    public ChessServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    public Game[] listGames(){
        var path = "/game";
        return null;
    }
    public Auth register(String name, String pass, String email){
        var path = "/user";
        return null;
    }
    public Auth login(String name, String pass){
        var path = "/session";
        return null;
    }
    public JoinGameResponse joinGame(int gameID, String color){
        var path = "/game";
        return null;
    }
    public CreateGameResponse createGame(String name){
        var path = "/game";
        return null;
    }
    public LogOutResponse logOut(){
        var path = "/session";
        return null;
    }
    public ClearResponse clearData(){
        var path = "/db";
        return null;
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
