package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import responses.GameListResponse;
import responses.ResponseException;
import services.GameServices;
import spark.Request;
import spark.Response;

public class GameListHandler {
    public GameListHandler() {
    }

    public String handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        var gson = new Gson();

        GameServices service = new GameServices();
        String authToken = req.headers("Authorization");
        GameListResponse result = service.getGames(authToken);
        res.status(200);
        var body = gson.toJson(result);
        res.body(body);
        return body;
    }
}
