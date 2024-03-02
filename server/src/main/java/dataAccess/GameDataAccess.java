package dataAccess;

import chess.ChessGame;
import models.Game;
import responses.ResponseException;

import java.util.List;

public interface GameDataAccess {
    void clearGames() throws DataAccessException;

    List<Game> listGames(String authToken) throws DataAccessException, ResponseException;
    int createGame(String name) throws DataAccessException, ResponseException;

    Game getGame(int id) throws DataAccessException, ResponseException;

    void saveGame(int gameID, ChessGame.TeamColor clientColor, String username) throws DataAccessException;

    boolean validColorToJoin(ChessGame.TeamColor color, ChessGame.TeamColor clientColor, String colorUsername);
}
