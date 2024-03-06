package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDataAccess;
import models.Auth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.ResponseException;
import services.ClearService;
import services.GameServices;
import services.UserServices;

import java.util.ArrayList;
import java.util.List;

public class dataAccessTests {
    private ClearService clearService;
    private UserServices userServices;
    private GameServices gameServices;
    private List<String> expected;
    private List<String> actual;

    @BeforeEach
    public void setUp() throws ResponseException, DataAccessException {
        clearService = new ClearService();
        userServices = new UserServices();
        gameServices = new GameServices();
        expected = new ArrayList<>();
        actual = new ArrayList<>();
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
    public void removingExistingAuthToken(){
        
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
