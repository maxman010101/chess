package dataAccess;

import chess.ChessGame;
import server.Game;
import server.User;
import server.Auth;

import java.util.List;

public interface DataAccess {
    boolean clearUsers() throws DataAccessException;

    boolean clearGames() throws DataAccessException;

    boolean clearAuths() throws DataAccessException;

    String getUser(String username, String passHash) throws DataAccessException;

    void createUser(String username, String passHash, String email) throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    boolean logOutUser(String authToken) throws DataAccessException;

    String getAuth(String authToken) throws DataAccessException;

    List<Game> listGames(String authToken) throws DataAccessException;

    boolean verifyLogIn(String authToken) throws DataAccessException;

    Game createGame(String name) throws DataAccessException;

    Game getGame(String id) throws DataAccessException;

    boolean saveGame(String clientColor, ChessGame game) throws DataAccessException;
}


