package dataAccess;

import models.Auth;
import models.Game;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryUserDataAccess implements UserDataAccess{
    static List<User> users = new ArrayList<>();

    static List<Auth> session = new ArrayList<>();
    public MemoryUserDataAccess() {
    }

    @Override
    public void clearUsers() throws DataAccessException {
        users.clear();
    }

    @Override
    public User getUser(String username, String password) throws DataAccessException {
        for (User user : users) {
            if (Objects.equals(user.username, username) && Objects.equals(user.password, password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        User newUser = new User(username, password, email);
        users.add(newUser);
    }

    @Override
    public Auth loginUser(String username, String authToken) throws DataAccessException {
        Auth auth = new Auth(authToken, username);
        session.add(auth);
        return auth;
    }

    @Override
    public void logOutUser(String authToken) throws DataAccessException {
        for(Auth auth : session){
            if(Objects.equals(auth.authToken, authToken)){
                session.remove(auth);
            }
        }
    }

    @Override
    public boolean verifyLogIn(String authToken) throws DataAccessException {
        for (Auth auth : session) {
            if (Objects.equals(auth.authToken, authToken)) {
                return true;
            }
        }
        return false;
    }
}
