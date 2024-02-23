package dataAccess;

import chess.ChessGame;
import models.Auth;
import models.Game;
import models.User;

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
        int gameID = games.size();
        Game game = new Game(games.size(), name, null, null, new ChessGame());
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
    public void saveGame(ChessGame.TeamColor clientColor, ChessGame game) throws DataAccessException {
        for (Game value : games) {
            if (Objects.equals(value.game, game)) {
                if (validColorToJoin(clientColor)){
                    value.game = game;
                }
            }
        }
    }

    @Override
    public boolean validColorToJoin(ChessGame.TeamColor color) {return true;}
}
