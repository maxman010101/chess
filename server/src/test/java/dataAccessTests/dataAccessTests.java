package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDataAccess;
import dataAccess.SQLGameDataAccess;
import dataAccess.SQLUserDataAccess;
import models.Auth;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.ResponseException;
import services.ClearService;
import services.GameServices;
import services.UserServices;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class dataAccessTests {
    private ClearService clearService;
    private UserServices userServices;
    private GameServices gameServices;
    @BeforeEach
    public void setUp() throws ResponseException, DataAccessException {
        clearService = new ClearService();
        userServices = new UserServices();
        gameServices = new GameServices();
        clearService.clearData();
    }
    //tests that check functionality of both createUser and getUser since they are connected in registering a User
    @Test
    public void successfulUserCreation() throws ResponseException, DataAccessException {
        //tests that check functionality of both createUser and getUser since they are connected in registering a User
        //success in registering a new user, getUser used to see if the user is in the database after registering
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        SQLUserDataAccess userDao = new SQLUserDataAccess();
        userDao.createUser(username,password,email);
        Assertions.assertNotNull(userDao.getUser(username));
    }
    @Test
    public void createExistingUser() throws ResponseException, DataAccessException {
        //trying to register with the same username as an existing user, if getUser isn't null with the new users username,
        // then the service with throw an exception that the username is already existing
        String username = "existingUsername";
        String password = "existingPass";
        String email = "existingEmail";
        String newUsername = "existingUsername";
        SQLUserDataAccess userDao = new SQLUserDataAccess();
        userDao.createUser(username,password,email);
        Assertions.assertNotNull(userDao.getUser(newUsername));
    }
    //testing the success and fail conditions of checkLogin method
    @Test
    public void loginIsGood() throws ResponseException, DataAccessException {
        SQLUserDataAccess userDao = new SQLUserDataAccess();
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        Auth user = userServices.register(username,password,email);
        String token = user.authToken;
        userServices.logout(token);
        Assertions.assertNotNull(userDao.checkLogin(username,password));
    }
    @Test
    public void loginWithWrongUsername() throws ResponseException, DataAccessException {
        SQLUserDataAccess userDao = new SQLUserDataAccess();
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        String wrongName = "newUser";
        Auth user = userServices.register(username,password,email);
        String token = user.authToken;
        userServices.logout(token);
        Assertions.assertNull(userDao.checkLogin(wrongName,password));
    }
    @Test
    public void getExistingUser() throws ResponseException, DataAccessException {
        SQLUserDataAccess userDao = new SQLUserDataAccess();
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        userServices.register(username,password,email);
        Assertions.assertNotNull(userDao.getUser(username));
    }
    @Test
    public void getFakeUser() throws ResponseException, DataAccessException {
        SQLUserDataAccess userDao = new SQLUserDataAccess();
        String nonExistingUsername = "myName";
        Assertions.assertNull(userDao.getUser(nonExistingUsername));
    }
    @Test
    public void createAGoodAuth() throws ResponseException, DataAccessException {
        SQLAuthDataAccess authDao = new SQLAuthDataAccess();
        String userName = "Maxman";
        String randomToken = "abc123";
        String token = authDao.createAuth(userName, randomToken).authToken;
        Assertions.assertEquals(randomToken, token);
    }
    @Test
    public void createAuthWithFakeToken() throws ResponseException, DataAccessException {
        //trying to create an Auth without an existing user or authToken
        SQLAuthDataAccess authDao = new SQLAuthDataAccess();
        boolean thrown = false;
        try {
            authDao.createAuth(null, null);
        } catch (ResponseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    @Test
    public void removingExistingAuthToken() throws ResponseException, DataAccessException {
        //testing the remove auth method as part of logging out
        SQLAuthDataAccess authDao = new SQLAuthDataAccess();
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        Auth user = userServices.register(username,password,email);
        String token = user.authToken;
        authDao.removeAuth(token);
        Assertions.assertNull(authDao.getAuth(token));
    }
    @Test
    public void removingNonExistingAuthtoken() throws ResponseException, DataAccessException {
        //trying to remove the wrong auth (aka logging out as one user when trying to log out as another),
        // and making sure the method removed the given token and not more or all of them from table
        String firstUser = "max";
        String randomAuthtoken = "abc123xyz890";
        String otherUser = "Bob";
        String otherAuthtoken = "asdfghjkl102938";
        SQLAuthDataAccess authDao = new SQLAuthDataAccess();
        authDao.createAuth(firstUser, randomAuthtoken);
        authDao.createAuth(otherUser,otherAuthtoken);
        authDao.removeAuth(randomAuthtoken);
        Assertions.assertNotNull(authDao.getAuth(otherAuthtoken));
    }
    @Test
    public void getARealAuth() throws ResponseException, DataAccessException {
        SQLAuthDataAccess authDao = new SQLAuthDataAccess();
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        Auth user = userServices.register(username,password,email);
        String token = user.authToken;
        Assertions.assertNotNull(authDao.getAuth(token));
    }
    @Test
    public void getUnrealAuth() throws ResponseException, DataAccessException {
        SQLAuthDataAccess authDao = new SQLAuthDataAccess();
        String fakeToken = "randomStuff";
        Assertions.assertNull(authDao.getAuth(fakeToken));
    }
    @Test
    public void createGoodGame() throws ResponseException, DataAccessException {
        //testing to see if the expected ID of the created game matched what the method returns
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        String gameName = "Maxman";
        Assertions.assertEquals(1, gameDao.createGame(gameName));
    }
    @Test
    public void createGameWithoutName() throws ResponseException, DataAccessException {
    //trying to make a game without giving it a name
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        boolean thrown = false;
        try {
            gameDao.createGame(null);
        } catch (ResponseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    @Test
    public void getAGame() throws ResponseException, DataAccessException {
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        String gameName = "Maxman";
        int gameID = gameDao.createGame(gameName);
        Assertions.assertNotNull(gameDao.getGame(gameID));
    }
    @Test
    public void getAGameWithoutRealID() throws ResponseException, DataAccessException {
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        Assertions.assertNull(gameDao.getGame(20));
    }
    @Test
    public void listOneGame() throws ResponseException, DataAccessException {
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        String gameName = "Maxman";
        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        gameDao.createGame(gameName);
        List<Game> games = gameDao.listGames(token);
        String createdName = games.getFirst().gameName;
        Assertions.assertEquals(gameName, createdName);
    }
    @Test
    public void listGamesWhenNoGamesExist() throws ResponseException, DataAccessException {
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        String username = "newUsername";
        String password = "newPass";
        String email = "newEmail";
        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        assertTrue((gameDao.listGames(token).isEmpty()));
    }
    @Test
    public void colorIsValid() throws ResponseException, DataAccessException {
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        String gameName = "Maxman";
        int gameID = gameDao.createGame(gameName);
        String whiteUser = gameDao.getGame(gameID).whiteUsername;
        Assertions.assertTrue(gameDao.validColorToJoin(ChessGame.TeamColor.WHITE, ChessGame.TeamColor.WHITE, whiteUser));
    }
    @Test
    public void colorNotValid() throws ResponseException, DataAccessException {
        //color isn't open to use as your team
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        String existingUsername = "Max";
        String gameName = "Maxman";
        int gameID = gameDao.createGame(gameName);
        gameDao.saveGame(gameID, ChessGame.TeamColor.WHITE, existingUsername);
        String whiteUser = gameDao.getGame(gameID).whiteUsername;
        Assertions.assertFalse(gameDao.validColorToJoin(ChessGame.TeamColor.WHITE, ChessGame.TeamColor.WHITE, whiteUser));
    }
    @Test
    public void gameIsSaved() throws ResponseException, DataAccessException {
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        String existingUsername = "Max";
        String gameName = "Maxman";
        int gameID = gameDao.createGame(gameName);
        gameDao.saveGame(gameID, ChessGame.TeamColor.WHITE, existingUsername);
        String whiteUser = gameDao.getGame(gameID).whiteUsername;
        Assertions.assertEquals(existingUsername, whiteUser);
    }
    @Test
    public void gameSavedWithBadID() throws ResponseException, DataAccessException {
        //trying to save a game without having a username to add to the game
        SQLGameDataAccess gameDao = new SQLGameDataAccess();
        String gameName = "Maxman";
        gameDao.createGame(gameName);
        boolean thrown = false;
        try {
            gameDao.saveGame(0, ChessGame.TeamColor.WHITE, "max");
        } catch (ResponseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    @Test
    public void clearAllDataSuccess() throws ResponseException, DataAccessException {
        String username = "maxman010101";
        String password = "KingSlayer123";
        String email = "abc123@gmail.com";
        String gameName = "winning";

        Auth user = userServices.register(username, password, email);
        String token = user.authToken;
        gameServices.createGame(token, gameName);
        //clearService's method uses the three clear methods in each sql dao so this test tests their functionality
        //if the service returns a null message then each one succeeded in clearing their respective data
        String message = clearService.clearData().message();
        Assertions.assertNull(message);
    }
}
