package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import responses.ResponseException;
import services.GameServices;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    public CreateGameHandler() {
    }

    public String handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        var gson = new Gson();

        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        GameServices service = new GameServices();
        String authToken = req.headers("Authorization");
        CreateGameResponse result = service.createGame(authToken, request.gameName());
        res.status(200);
        var body = gson.toJson(result);
        res.body(body);
        return body;
    }
}
