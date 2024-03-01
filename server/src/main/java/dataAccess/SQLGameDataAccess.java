package dataAccess;

import chess.ChessGame;
import models.Game;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class SQLGameDataAccess implements GameDataAccess{
    public SQLGameDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @Override
    public void clearGames() throws DataAccessException {

    }

    @Override
    public List<Game> listGames(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public int createGame(String name) throws DataAccessException {
        return 0;
    }

    @Override
    public Game getGame(int id) throws DataAccessException {
        return null;
    }

    @Override
    public void saveGame(int gameID, ChessGame.TeamColor clientColor, String username) throws DataAccessException {

    }

    @Override
    public boolean validColorToJoin(ChessGame.TeamColor color, ChessGame.TeamColor clientColor, String colorUsername) {
        return false;
    }
}
