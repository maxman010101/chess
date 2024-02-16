package server;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import javax.imageio.spi.RegisterableService;

public class RegisterHandler {

    public RegisterHandler() {
    }

    public String handleRequest(Request req, Response res) {
        var gson = new Gson();
        //ClearRequest request = (ClearRequest)gson.fromJson(reqData, ClearResponse.class);

        UserServices service = new UserServices();
        RegisterResponse result = service.register();
        if(result.message() == null) {
            res.status(200);
        }
        else {
            res.status(403);
        }
        var body = gson.toJson(result);
        res.body(body);
        return body;
    }
}
