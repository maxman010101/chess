package handlers;

import com.google.gson.Gson;
import models.Auth;
import requests.LoginRequest;
import responses.ResponseException;
import services.UserServices;
import spark.Request;
import spark.Response;

public class LoginHandler {
    public LoginHandler() {
    }

    public String handleRequest(Request req, Response res) throws ResponseException {
        var gson = new Gson();
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);

        UserServices service = new UserServices();
        Auth result = service.login(request.username(), request.password());
        res.status(200);
        var body = gson.toJson(result);
        res.body(body);
        return body;
    }
}
