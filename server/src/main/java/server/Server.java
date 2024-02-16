package server;

import dataAccess.DataAccessException;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        desiredPort = 8080;
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> (new ClearHandler()).handleRequest(req, res));
        //Spark.delete("/db", this::clear);



        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}