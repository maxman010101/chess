package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDataAccess;
import dataAccess.MemoryGameDataAccess;
import dataAccess.MemoryUserDataAccess;
import models.Auth;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.CreateGameResponse;
import responses.ResponseException;
import services.ClearService;
import services.GameServices;
import services.UserServices;

import java.util.ArrayList;
import java.util.List;

public class ServiceUnitTests {
    private ClearService clearService;
    private UserServices userServices;
    private GameServices gameServices;
    private List<String> expected;
    private List<String> actual;


    @BeforeEach
    public void setUp(){
        clearService = new ClearService();
        userServices = new UserServices();
        gameServices = new GameServices();
        expected = new ArrayList<>();
        actual = new ArrayList<>();

    }
    @Test
    public void registerSuccess() throws ResponseException {
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        expected.add("newUsername");
        Auth registeredUser = userServices.register(username, password, email);
        String actualUsername = registeredUser.username;
        actual.add(actualUsername);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void registerFail() throws ResponseException, DataAccessException {
        String existingUsername = "newUsername";
        String existingPassword = "newPass";
        String existingEmail = "newEmail";
        String secondUsername = "newUsername";

        //registering with an already existing username

        userServices.register(existingUsername, existingPassword, existingEmail);
        MemoryUserDataAccess userDoa = new MemoryUserDataAccess();
        Assertions.assertNotNull(userDoa.getUser(secondUsername));
    }
    @Test
    public void loginSuccess() throws ResponseException, DataAccessException {
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";

        userServices.register(username, password, email);
        MemoryUserDataAccess userDoa = new MemoryUserDataAccess();
        Assertions.assertNotNull(userDoa.checkLogin(username, password));
    }

    @Test
    public void loginFail() throws ResponseException, DataAccessException {
        String username = "newUsername";
        String registeredPassword = "newPass";
        String email = "newEmail";
        String wrongPassword = "mewPas";

        //logging in with wrong password

        userServices.register(username, registeredPassword, email);
        MemoryUserDataAccess userDoa = new MemoryUserDataAccess();
        Assertions.assertNull(userDoa.checkLogin(username, wrongPassword));
    }

    @Test
    public void logoutSuccess() throws ResponseException, DataAccessException {
        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";

        Auth user = userServices.register(username, password, email);
        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();
        userServices.login(username, password);
        String token = user.authToken;
        userServices.logout(token);
        Assertions.assertNull(authDoa.getAuth(token));
    }

    @Test
    public void logoutFail() throws ResponseException, DataAccessException {

        //failing a logout by not even logging in or registering first

        String authToken = "randomStringToken";

        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();
        Assertions.assertNull(authDoa.getAuth(authToken));
    }

    @Test
    public void createGameSuccess() throws ResponseException, DataAccessException {
        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";
        String gameName = "winning";
        int gameID = 1;

        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        CreateGameResponse game = gameServices.createGame(token, gameName);
        Assertions.assertEquals(gameID, game.gameID());
    }

    @Test
    public void createGameFail() throws ResponseException, DataAccessException {
        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";

        //trying to make a game without logging in/when you are logged out(part of the create game method fails if getAuth returns null)

        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        userServices.logout(token);
        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();
        Assertions.assertNull(authDoa.getAuth(token));
    }

    @Test
    public void listGameSuccess() throws ResponseException, DataAccessException {

        //list one game

        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";
        String gameName = "theGame";


        MemoryGameDataAccess gameDoa = new MemoryGameDataAccess();
        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        gameDoa.createGame(gameName);
        expected.add(gameName);
        List<Game> games = gameDoa.listGames(token);
        Assertions.assertEquals(expected.getFirst(), games.getFirst().gameName);
    }

    @Test
    public void listGameFail() throws ResponseException, DataAccessException {

        //failed getting game list if you are not logged in(list game fails if getAuth returns null)

        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";

        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        userServices.logout(token);
        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();
        Assertions.assertNull(authDoa.getAuth(token));
    }

    @Test
    public void joinGameSuccess() throws ResponseException, DataAccessException {

        //your team color is open to join

        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";
        String gameName = "winning";

        MemoryGameDataAccess gameDoa = new MemoryGameDataAccess();
        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        CreateGameResponse gameID = gameServices.createGame(token, gameName);
        gameServices.joinGame(token, ChessGame.TeamColor.BLACK, gameID.gameID());
        Game joinedGame = gameDoa.getGame(gameID.gameID());
        Assertions.assertEquals(username, joinedGame.blackUsername);
    }

    @Test
    public void joinGameFail() throws ResponseException, DataAccessException {
        //your team color is not open to join

        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";
        String otherUsername = "bobby";
        String otherPass = "rookie";
        String otherEmail = "joeJoe";
        String gameName = "winning";

        MemoryGameDataAccess gameDoa = new MemoryGameDataAccess();
        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        CreateGameResponse gameID = gameServices.createGame(token, gameName);
        gameServices.joinGame(token, ChessGame.TeamColor.BLACK, gameID.gameID());
        userServices.register(otherUsername, otherPass, otherEmail);
        boolean openSpot = gameDoa.validColorToJoin(ChessGame.TeamColor.BLACK, ChessGame.TeamColor.BLACK, otherUsername);
        Assertions.assertFalse(openSpot);
    }

    @Test
    public void clearSuccess() throws ResponseException, DataAccessException {
        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";
        String gameName = "winning";

        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        gameServices.createGame(token, gameName);
        String message = clearService.clearData().message();
        Assertions.assertNull(message);
    }
}
