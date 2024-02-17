package server;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;


public class RegisterHandler {

    public RegisterHandler() {
    }

    public String handleRequest(Request req, Response res) throws ResponseException {
        var gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

        UserServices service = new UserServices();
        Auth result = service.register(request.username(), request.password(), request.email());
        res.status(200);
        var body = gson.toJson(result);
        res.body(body);
        return body;
    }
}
