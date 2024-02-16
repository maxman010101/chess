package dataAccess;

import chess.ChessGame;
import server.Game;
import server.User;
import server.Auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoryDataAccess implements DataAccess{

    static List<Arrays> users = new ArrayList<>();

    static List<Arrays> auths = new ArrayList<>();
    static List<Arrays> session = new ArrayList<>();
    static List<Arrays> game = new ArrayList<>();

    public MemoryDataAccess() {
    }

    public void clearAll() throws DataAccessException {
        users.clear();
        auths.clear();
        game.clear();
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
