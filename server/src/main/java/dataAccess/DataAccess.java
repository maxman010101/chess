package dataAccess;

import chess.ChessGame;
import server.Auth;
import server.Game;
import server.User;

import java.util.List;

public interface DataAccess {
    void clearAll() throws DataAccessException;

    User getUser(String username, int passHash) throws DataAccessException;

    void createUser(String username, int passHash, String email) throws DataAccessException;

    Auth createAuth(String username, String authToken) throws DataAccessException;

    boolean logOutUser(String authToken) throws DataAccessException;

    String getAuth(String authToken) throws DataAccessException;

    List<Game> listGames(String authToken) throws DataAccessException;

    boolean verifyLogIn(String authToken) throws DataAccessException;

    Game createGame(String name) throws DataAccessException;

    Game getGame(String id) throws DataAccessException;

    boolean saveGame(String clientColor, ChessGame game) throws DataAccessException;
}


