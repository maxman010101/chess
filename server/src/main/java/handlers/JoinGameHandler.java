package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import requests.JoinGameRequest;
import responses.JoinGameResponse;
import responses.ResponseException;
import services.GameServices;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    public JoinGameHandler() {
    }

    public String handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        var gson = new Gson();

        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        GameServices service = new GameServices();
        String authToken = req.headers("Authorization");
        JoinGameResponse result = service.joinGame(authToken, request.playerColor(), request.gameID());
        res.status(200);
        var body = gson.toJson(result);
        res.body(body);
        return body;
    }
}
