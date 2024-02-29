package dataAccess;

import chess.ChessGame;
import models.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryGameDataAccess implements GameDataAccess{
    static List<Game> games = new ArrayList<>();
    public MemoryGameDataAccess() {
    }

    @Override
    public void clearGames() throws DataAccessException {
        games.clear();
    }

    @Override
    public List<Game> listGames(String authToken) throws DataAccessException {
        return games;
    }

    @Override
    public int createGame(String name) throws DataAccessException {
        int gameID = games.size()+1;
        Game game = new Game(gameID, name, null, null, new ChessGame());
        games.add(game);
        return gameID;
    }

    @Override
    public Game getGame(int id) throws DataAccessException {
        for (Game game : games) {
            if (Objects.equals(game.gameID, id)) {
                return game;
            }
        }
        return null;
    }

    @Override
    public void saveGame(int gameID, ChessGame.TeamColor clientColor, String username) throws DataAccessException {
        for (Game g : games) {
            if (Objects.equals(g.gameID, gameID)) {
                if (clientColor == ChessGame.TeamColor.BLACK){
                    g.blackUsername = username;
                }
                if (clientColor == ChessGame.TeamColor.WHITE){
                    g.whiteUsername = username;
                }
            }
        }
    }

    @Override
    public boolean validColorToJoin(ChessGame.TeamColor color, ChessGame.TeamColor clientColor, String colorUsername) {
        return (clientColor == color && colorUsername == null);
    }
}
