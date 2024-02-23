package dataAccess;

import chess.ChessGame;
import models.Game;

import java.util.List;

public interface GameDataAccess {
    void clearGames() throws DataAccessException;

    List<Game> listGames(String authToken) throws DataAccessException;
    int createGame(String name) throws DataAccessException;

    Game getGame(int id) throws DataAccessException;

    void saveGame(ChessGame.TeamColor clientColor, ChessGame game) throws DataAccessException;

    boolean validColorToJoin(ChessGame.TeamColor color);
}
