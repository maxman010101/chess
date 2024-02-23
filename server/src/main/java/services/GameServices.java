package services;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDataAccess;
import dataAccess.MemoryGameDataAccess;
import models.Game;
import responses.CreateGameResponse;
import responses.GameListResponse;
import responses.LogOutResponse;
import responses.ResponseException;

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
        }
        catch(DataAccessException | ResponseException e){
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
        catch(DataAccessException | ResponseException e){
            throw new ResponseException("Error: cannot access DB", 500);
        }
    }
}

