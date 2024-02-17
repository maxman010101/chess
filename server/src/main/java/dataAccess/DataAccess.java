package dataAccess;

import chess.ChessGame;
import server.Auth;
import server.Game;
import server.User;

import java.util.List;

public interface DataAccess {
    void clearAll() throws DataAccessException;

    User getUser(String username) throws DataAccessException;

    void createUser(String username, String password, String email) throws DataAccessException;

    Auth createAuth(String username, String authToken) throws DataAccessException;

    void logOutUser(String authToken) throws DataAccessException;

    Auth getAuth(String authToken) throws DataAccessException;

    List<Game> listGames(String authToken) throws DataAccessException;

    boolean verifyLogIn(String authToken) throws DataAccessException;

    Game createGame(String name, String whiteUser, String blackUser, ChessGame chessgame) throws DataAccessException;

    Game getGame(int id) throws DataAccessException;

    void saveGame(String clientColor, ChessGame game) throws DataAccessException;

    boolean validColorToJoin(String color);
}


