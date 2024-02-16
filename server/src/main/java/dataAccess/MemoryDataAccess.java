package dataAccess;

import chess.ChessGame;
import server.Auth;
import server.Game;
import server.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryDataAccess implements DataAccess{

    static List<User> users = new ArrayList<>();

    static List<Auth> auths = new ArrayList<>();
    static List<Auth> session = new ArrayList<>();
    static List<Game> game = new ArrayList<>();

    public MemoryDataAccess() {
    }

    public void clearAll() throws DataAccessException {
        users.clear();
        auths.clear();
        game.clear();
    }


    @Override
    public User getUser(String username, int passHash) throws DataAccessException {
        for(int i = 0; i <= users.size(); i++){
            if(Objects.equals(users.get(i).username, username)){
                return users.get(i);
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, int passHash, String email) throws DataAccessException {
        User newUser = new User(username, passHash, email);
        users.add(newUser);
    }

    @Override
    public Auth createAuth(String username, String authToken) throws DataAccessException {
        Auth auth = new Auth(authToken, username);
        auths.add(auth);
        return auth;
    }

    @Override
    public boolean logOutUser(String authToken) throws DataAccessException {
        return false;
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public List<Game> listGames(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public boolean verifyLogIn(String authToken) throws DataAccessException {
        return false;
    }

    @Override
    public Game createGame(String name) throws DataAccessException {
        return null;
    }

    @Override
    public Game getGame(String id) throws DataAccessException {
        return null;
    }

    @Override
    public boolean saveGame(String clientColor, ChessGame game) throws DataAccessException {
        return false;
    }
}
