package dataAccess;

import chess.ChessGame;

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

    List<ChessGame> listGames(String authToken) throws DataAccessException;

    boolean verifyLogIn(String authToken) throws DataAccessException;

    ChessGame createGame(String name) throws DataAccessException;

    ChessGame getGame(String id) throws DataAccessException;

    boolean saveGame(String clientColor, ChessGame game) throws DataAccessException;
}


