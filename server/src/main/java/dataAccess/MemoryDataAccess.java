package dataAccess;

import chess.ChessGame;
import server.Game;
import server.User;
import server.Auth;

import java.util.Arrays;
import java.util.List;

public class MemoryDataAccess implements DataAccess{

    static List<Arrays> users;
    static List<Arrays> auths;
    static List<Arrays> session;
    static List<Arrays> game;

    public MemoryDataAccess() {
    }

    @Override
    public boolean clearUsers() throws DataAccessException {
        users.clear();
        return true;
    }

    @Override
    public boolean clearGames() throws DataAccessException {
        game.clear();
        return true;
    }

    @Override
    public boolean clearAuths() throws DataAccessException {
        auths.clear();
        return true;
    }

    @Override
    public String getUser(String username, String passHash) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(String username, String passHash, String email) throws DataAccessException {

    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        return null;
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
