package services;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDataAccess;
import dataAccess.MemoryGameDataAccess;
import models.Game;
import responses.*;

import java.util.List;

public class GameServices {
    public GameServices() {
    }

    public CreateGameResponse createGame(String authToken, String gameName) throws DataAccessException, ResponseException{
        MemoryGameDataAccess gameDoa = new MemoryGameDataAccess();
        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();

        if(gameName == null){throw new ResponseException("Error: bad request", 400);}
        try{
            if(authDoa.getAuth(authToken) != null){
                int gameID = gameDoa.createGame(gameName);
                return new CreateGameResponse(gameID,null);
            }

            else{
                throw new ResponseException("Error: unauthorized", 401);
            }
        } catch(DataAccessException e){
            throw new ResponseException("Error: cannot access DB", 500);
        }
    }

    public GameListResponse getGames(String authToken) throws DataAccessException, ResponseException {
        MemoryGameDataAccess gameDoa = new MemoryGameDataAccess();
        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();
        try{
            if(authDoa.getAuth(authToken) != null){
                List<Game> games = gameDoa.listGames(authToken);
                return new GameListResponse(games,null);
            }

            else{
                throw new ResponseException("Error: unauthorized", 401);
            }
        }
        catch(DataAccessException e){
            throw new ResponseException("Error: cannot access DB", 500);
        }
    }

    public JoinGameResponse joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws ResponseException {
        MemoryGameDataAccess gameDoa = new MemoryGameDataAccess();
        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();

        if(gameID < 1){throw new ResponseException("Error: bad request", 400);}
        try{
            if(authDoa.getAuth(authToken) != null){
                Game game = gameDoa.getGame(gameID);
                if(gameDoa.validColorToJoin(ChessGame.TeamColor.WHITE, playerColor, game.whiteUsername)
                        || gameDoa.validColorToJoin(ChessGame.TeamColor.BLACK, playerColor, game.blackUsername) || playerColor == null){
                gameDoa.saveGame(gameID, playerColor, authDoa.getAuth(authToken).username);
                return new JoinGameResponse(null);
                }
                else
                    throw  new ResponseException("Error: already taken", 403);
            }

            else{
                throw new ResponseException("Error: unauthorized", 401);
            }
        } catch(DataAccessException e){
            throw new ResponseException("Error: cannot access DB", 500);
        }
    }
}


