package clientTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDataAccess;
import org.junit.jupiter.api.*;
import server.Server;
import services.ClearService;
import ui.ChessServerFacade;
import ui.ResponseException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ChessServerFacade facade;
    private ClearService clearService = new ClearService();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ChessServerFacade("http://localhost:8080");
    }
    @BeforeEach
    public void clearData() throws responses.ResponseException, DataAccessException {
        clearService.clearData();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void validRegister() throws ResponseException {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken.length() > 10);
    }
    @Test
    public void invalidRegister() throws ResponseException, responses.ResponseException, DataAccessException {
        //cannot register with the same username as an existing user
        facade.register("a", "b", "c");
        SQLUserDataAccess userDao = new SQLUserDataAccess();
        Assertions.assertNotNull(userDao.getUser("a"));
    }
    
}
