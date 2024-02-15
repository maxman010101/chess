package dataAccess;

import chess.ChessGame;

import java.util.List;

public class MemoryDataAccess implements DataAccess{

    @Override
    public boolean clearUsers() throws DataAccessException {
        return false;
    }

    @Override
    public boolean clearGames() throws DataAccessException {
        return false;
    }

    @Override
    public boolean clearAuths() throws DataAccessException {
        return false;
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
    public List<ChessGame> listGames(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public boolean verifyLogIn(String authToken) throws DataAccessException {
        return false;
    }

    @Override
    public ChessGame createGame(String name) throws DataAccessException {
        return null;
    }

    @Override
    public ChessGame getGame(String id) throws DataAccessException {
        return null;
    }

    @Override
    public boolean saveGame(String clientColor, ChessGame game) throws DataAccessException {
        return false;
    }
}
