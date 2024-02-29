package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import responses.LogOutResponse;
import responses.ResponseException;
import services.UserServices;
import spark.Request;
import spark.Response;

public class LogOutHandler {
    public LogOutHandler() {
    }
    public String handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        var gson = new Gson();

        UserServices service = new UserServices();
        String authToken = req.headers("Authorization");
        LogOutResponse result = service.logout(authToken);
        res.status(200);
        var body = gson.toJson(result);
        res.body(body);
        return body;
    }
}
