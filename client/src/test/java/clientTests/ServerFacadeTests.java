package clientTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDataAccess;
import dataAccess.SQLGameDataAccess;
import dataAccess.SQLUserDataAccess;
import org.junit.jupiter.api.*;
import server.Server;
import services.ClearService;
import ui.ChessServerFacade;
import ui.ResponseException;

import static org.junit.jupiter.api.Assertions.*;
import static ui.State.SIGNEDOUT;


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
    @Test
    public void goodLogin() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        facade.logOut();
        var authData = facade.login("player1", "password");
        assertTrue(authData.authToken.length() > 10);
    }
    @Test
    public void badLogin() throws ResponseException, responses.ResponseException, DataAccessException {
        //cannot log in if the user isn't registered
        facade.register("a", "b", "c");
        facade.logOut();
        SQLUserDataAccess userDao = new SQLUserDataAccess();
        Assertions.assertNotNull(userDao.getUser("a"));
        Assertions.assertNull(userDao.getUser("otherName"));
    }
    @Test
    public void goodLogOut() throws ResponseException, responses.ResponseException, DataAccessException {
        var token = facade.register("a", "b", "c");
        facade.logOut();
        SQLAuthDataAccess authDao = new SQLAuthDataAccess();
        assertNull(authDao.getAuth(token.authToken));
    }
    @Test
    public void badLogOut() {
        //logging out without logging in or registering
        boolean thrown = false;
        try {
            facade.logOut();
        } catch (ResponseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    @Test
    public void madeGoodGame() throws ResponseException {
        facade.register("a", "b", "c");
        var game = facade.createGame("game1");
        assertEquals(1, game.gameID());
    }
    @Test
    public void gameCreationFailed() throws ResponseException {
        //trying to create an unnamed game
        facade.register("a", "b", "c");
        boolean thrown = false;
        try {
            facade.createGame(null);
        } catch (ResponseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    @Test
    public void gameListed() throws ResponseException {
        //game created is listed
        facade.register("a", "b", "c");
        facade.createGame("game1");
        assertNotNull(facade.listGames().games().getFirst());
    }
    @Test
    public void gamesNotListed() throws ResponseException {
        //trying to list nobody's games/logged out when trying to make the command
        facade.register("a", "b", "c");
        facade.logOut();
        boolean thrown = false;
        try {
            facade.listGames();
        } catch (ResponseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    @Test
    public void joinedAGame() throws ResponseException {
        facade.register("a", "b", "c");
        var game = facade.createGame("game1");
        boolean thrown = false;
        try {
            facade.joinGame(game.gameID(), ChessGame.TeamColor.WHITE, facade.listGames().games().getFirst(), null);
        } catch (ResponseException e) {
            thrown = true;
        }
        assertFalse(thrown);
    }
    @Test
    public void tryToJoinFakeGame() throws ResponseException {
        facade.register("a", "b", "c");
        facade.createGame("game1");
        boolean thrown = false;
        try {
            facade.joinGame(2, ChessGame.TeamColor.WHITE, facade.listGames().games().getFirst(), null);

        } catch (ResponseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
