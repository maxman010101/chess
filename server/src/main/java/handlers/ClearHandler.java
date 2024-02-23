package handlers;

import com.google.gson.Gson;
import responses.ClearResponse;
import services.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public ClearHandler() {
    }

    public String handleRequest(Request req, Response res) {
        var gson = new Gson();

        ClearService service = new ClearService();
        ClearResponse result = service.clearData();
        if(result.message() == null) {
            res.status(200);
        }
        else{
            res.status(500);
        }
        var body = gson.toJson(result);
        res.body(body);
        return body;
    }
}
